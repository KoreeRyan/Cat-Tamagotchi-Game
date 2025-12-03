import java.time.Instant;

public class Cat {

    //Stats
    private int fullness;
    private int happiness;
    private int energy;
    private int cleanliness;

    //State
    private boolean isSleeping;
    private Instant lastInteractionTime = Instant.now();
    private Instant sleepStartTime;
    private Instant lastHappinessDecayTime;



    public Cat() {
        this.fullness = 100;
        this.happiness = 50;
        this.energy = 100;
        this.cleanliness = 100;
        this.lastInteractionTime = Instant.now();
        this.isSleeping = false;
        this.sleepStartTime = null;
    }

    public void feed() {
        if (isSleeping) {
            if (energy == 0) {
                return;
            } else {
                wakeUp();

            }
        }

        fullness = 100;
        cleanliness -= 20;
        energy = (energy + 100) / 2;
        lastInteractionTime = Instant.now();

        fullness = clampStat(fullness);
        happiness = clampStat(happiness);
        energy = clampStat(energy);
        cleanliness = clampStat(cleanliness);
    }

    public void play() {
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

    }

    public void clean() {
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
    }

    public void updateStatus() {
        Instant now = Instant.now();

        //1) Wake up from exhaustion sleep after 5 minutes
        if (isSleeping && energy == 0 && sleepStartTime != null) {
            // 5 minutes = 5 * 60 seconds
            Instant wakeTime = sleepStartTime.plusSeconds(5 * 60);
            if (now.isAfter(wakeTime) || now.equals(wakeTime)) {
                energy = 75;
                isSleeping = false;
                sleepStartTime = null;
            }
        }

        //2) Fall asleep from idleness after 10 minutes (only if awake)
        if (!isSleeping && lastInteractionTime != null) {
            // 10 minutes = 10 * 60 seconds
            Instant idleSleepTime = lastInteractionTime.plusSeconds(10 * 60);
            if (now.isAfter(idleSleepTime) || now.equals(idleSleepTime)) {
                isSleeping = true;
                sleepStartTime = now;
            }

        }

        //3) Happiness decay when cleanliness is 0 (1 point every 5 seconds)
        if (cleanliness == 0) {
            if (lastHappinessDecayTime == null) {
                lastHappinessDecayTime = now;
            } else {
                Instant nextDecayTime = lastHappinessDecayTime.plusSeconds(5);
                if (now.isAfter(nextDecayTime) || now.equals(nextDecayTime)) {
                    happiness -= 1;
                    happiness = clampStat(happiness);
                    lastHappinessDecayTime = now;
                }
            }
        } else {
            lastHappinessDecayTime = null;
        }

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
