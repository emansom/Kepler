package org.alexdev.kepler.game.games.snowstorm;

import org.alexdev.kepler.game.games.GameObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TurnContainer {
    private AtomicInteger currentTurn;
    private List<GameObject> objectList;
    private int checkSum;

    public TurnContainer() {
        this.currentTurn = new AtomicInteger(0);
    }

    public void calculateChecksum(List<GameObject> objectList) {
        this.objectList = objectList;

        int tCheckSum;

        tCheckSum = iterateSeed(this.currentTurn.get());
        tCheckSum = calculateChecksum(tCheckSum);

        this.checkSum = tCheckSum;
    }

    private int calculateChecksum(int tSeed) {
        int tCheckSum = tSeed;

        for (var object : this.objectList) {
            SnowStormGameObject gameObject = (SnowStormGameObject) object;
            tCheckSum = tCheckSum + addChecksum(gameObject.getGameObjectsSyncValues());
        }

        return tCheckSum;
    }

    private int addChecksum(List<Integer> pGameObjectSyncValues) {
        int tCheckSum = 0;
        int tCounter = 1;
        int i = 0;

        for (int value : pGameObjectSyncValues) {
            tCheckSum = tCheckSum + (value * tCounter);
            tCounter = tCounter + 1;
        }

        return tCheckSum;
    }

    private int iterateSeed(int a_iSeed) {
        var t_iSeed2 = 0;

        if (a_iSeed == 0) {
            a_iSeed = -1;
        }

        t_iSeed2 = a_iSeed << 13;
        a_iSeed = a_iSeed ^ t_iSeed2;
        t_iSeed2 = a_iSeed >> 17;
        a_iSeed = a_iSeed ^ t_iSeed2;
        t_iSeed2 = a_iSeed << 5;
        a_iSeed = a_iSeed ^ t_iSeed2;

        return a_iSeed;
    }

    public AtomicInteger getCurrentTurn() {
        return currentTurn;
    }

    public int getCheckSum() {
        return checkSum;
    }
}
