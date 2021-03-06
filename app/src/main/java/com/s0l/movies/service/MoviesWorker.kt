package com.s0l.movies.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.annotation.ExperimentalCoilApi
import com.s0l.movies.data.usecases.network.MovieNetworkInteractor
import com.s0l.movies.data.usecases.network.TopRatedMovieInteractor
import javax.inject.Inject

class MoviesWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params)
{

    @Inject
    lateinit var moviesInteractor: MovieNetworkInteractor
    @Inject
    lateinit var topRatedMovieInteractor: TopRatedMovieInteractor
    @Inject
    lateinit var notifications: MovieNotifications

    init {
        notifications.initialize()
    }

    @ExperimentalCoilApi
    override suspend fun doWork(): Result {
        return try {
            moviesInteractor.loadMoviesAndSave()
            topRatedMovieInteractor.getNewTopRatedMovie()?.let { movie ->
                notifications.show(movie)
            }
            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }

}