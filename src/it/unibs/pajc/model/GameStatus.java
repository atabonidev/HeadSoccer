package it.unibs.pajc.model;

public enum GameStatus {
    PLAYING,
    LOADING;

    public static GameStatus gameState = LOADING; //lo stato con cui si comincia

    public static void SetGameState(GameStatus newState) {
        gameState = newState;
    }
}
