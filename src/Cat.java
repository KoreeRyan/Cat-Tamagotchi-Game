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
        if (isSleeping) {
            if (energy == 0) {}
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
