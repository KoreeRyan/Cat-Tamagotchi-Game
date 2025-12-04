import java.time.Duration;
import java.time.Instant;

public class Cat {

    //Stats
    private int fullness;
    private int happiness;
    private int energy;
    private int cleanliness;

    //State
    private boolean isSleeping;
    private boolean gameOver;
    private Instant lastInteractionTime;
    private Instant sleepStartTime;
    private Instant lastHappinessDecayTime;




    public Cat() {
        this.fullness = 100;
        this.happiness = 50;
        this.energy = 100;
        this.cleanliness = 100;
        this.lastInteractionTime = Instant.now();
        this.isSleeping = false;
        this.gameOver = false;
        this.sleepStartTime = null;
    }


    public void feed() {
        if (gameOver) {
            return;
        }
        if (isSleeping) {
            if (energy == 0) {
                return;
            } else {
                wakeUp();

            }
        }

        fullness = 100;
        cleanliness -= 20;
        energy += 25;
        lastInteractionTime = Instant.now();


        if (energy <= 0) {
            energy = 0;
            isSleeping = true;
            sleepStartTime = Instant.now();
        }

        lastInteractionTime = Instant.now();

        fullness = clampStat(fullness);
        happiness = clampStat(happiness);
        energy = clampStat(energy);
        cleanliness = clampStat(cleanliness);

        checkGameOver();
    }

    public void play() {
        if (gameOver) {
            return;
        }
        if (isSleeping) {
            if (energy == 0) {
                return;
            } else {
                wakeUp();
            }
        }

        fullness -= 25;
        happiness += 25;
        energy -= 30;

        //If energy hits 0 or below, trigger exhaustion sleep
        if (energy <= 0) {
            energy = 0;
            isSleeping = true;
            sleepStartTime = Instant.now();
        }

        lastInteractionTime = Instant.now();

        fullness = clampStat(fullness);
        happiness = clampStat(happiness);
        energy = clampStat(energy);
        cleanliness = clampStat(cleanliness);

        checkGameOver();

    }

    public void clean() {
        if (gameOver) {
            return;
        }
        if (isSleeping) {
            if (energy == 0) {
                return;
            } else {
                wakeUp();
            }
        }

        happiness += 25;
        cleanliness = 100;
        lastInteractionTime = Instant.now();

        fullness = clampStat(fullness);
        happiness = clampStat(happiness);
        energy = clampStat(energy);
        cleanliness = clampStat(cleanliness);

        checkGameOver();
    }

    public void updateStatus() {
        Instant now = Instant.now();

        if (gameOver) {
            return;
        }

        //1) Wake up from exhaustion sleep after 5 seconds
        if (isSleeping && energy == 0 && sleepStartTime != null) {
            Instant wakeTime = sleepStartTime.plusSeconds(10);

            if (now.isAfter(wakeTime)) {
                energy = 75;
                isSleeping = false;
                sleepStartTime = null;
            }

        }

        //2) Fall asleep from idleness after 10 seconds
        if (!isSleeping && energy > 0 &&lastInteractionTime != null) {

            if (Duration.between(lastInteractionTime, now).getSeconds() < 2) {
                return;
            }
            Instant idleSleepTime = lastInteractionTime.plusSeconds(10);

            if (now.isAfter(idleSleepTime))  {
                isSleeping = true;
                sleepStartTime = now;
            }

        }

        //3) Happiness decay when cleanliness is 0 (1 point every 5 seconds)
        if (cleanliness == 0) {
            if (lastHappinessDecayTime == null) {
                lastHappinessDecayTime = now;
            } else {
                Instant nextDecayTime = lastHappinessDecayTime.plusSeconds(1);
                if (now.isAfter(nextDecayTime) || now.equals(nextDecayTime)) {
                    happiness -= 1;
                    happiness = clampStat(happiness);
                    lastHappinessDecayTime = now;
                }
            }
        } else {
            lastHappinessDecayTime = null;
        }

         checkGameOver();
    }


    public int getFullness() {
        return fullness;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getEnergy() {
        return energy;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void resetInteractionTimer() {
        lastInteractionTime = Instant.now();
    }

    private void checkGameOver() {
        if (fullness == 0 || happiness == 0) {
            fullness = 0;
            happiness = 0;
            energy = 0;
            cleanliness = 0;
            gameOver = true;
        }
    }

    //Helper to keep stats between 0 and 100
    private int clampStat(int value) {
        if (value < 0) {
            return 0;
        }
        return Math.min(value, 100);
    }

        public void wakeUp() {
            isSleeping = false;
    }
}
