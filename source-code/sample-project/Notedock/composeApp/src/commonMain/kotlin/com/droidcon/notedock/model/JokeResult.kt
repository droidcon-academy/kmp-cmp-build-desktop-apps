package com.droidcon.notedock.model

sealed class JokeResult {
    //Successful fetch
    data class Success(val joke: Joke) : JokeResult()

    //In case of error
    data class Error(val message: String): JokeResult()

    //Indicates loading
    object Loading: JokeResult()

    //Inactive
    object NotStarted: JokeResult()
}