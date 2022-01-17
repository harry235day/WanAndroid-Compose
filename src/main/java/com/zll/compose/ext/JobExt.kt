package com.zll.compose.ext

import kotlinx.coroutines.Job

fun Job.addTo(jobs: MutableList<Job>){
    jobs.add(this)
}