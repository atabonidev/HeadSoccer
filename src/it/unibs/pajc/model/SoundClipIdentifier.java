package it.unibs.pajc.model;

import java.io.Serializable;

/**
 * Classe che gestisce il singolo suono tra client e server.
 */

public class SoundClipIdentifier implements Serializable {

    private int clipNumber; //identifica il suono fra quelli presenti nel gioco
    private boolean isClipActive; //indica se il suono è attualmente attivo

    public SoundClipIdentifier() {
        clipNumber = -1;
        isClipActive = true;
    }

    public SoundClipIdentifier(int clipNumber, boolean isClipActive) {
        this.clipNumber = clipNumber;
        this.isClipActive = isClipActive;
    }

    public int getClipNumber() {
        return clipNumber;
    }

    public boolean isClipActive() {
        return isClipActive;
    }

    public void setClipActive(boolean clipActive) {
        isClipActive = clipActive;
    }

    public void setClipNumber(int clipNumber) {
        this.clipNumber = clipNumber;
    }
}
