package com.example.spacegame.com.example.spacegame

import android.content.Context
import com.example.spacegame.com.example.spacegame.utils.SignalManager

class GameManager {
    private val alienPositions = intArrayOf(-1, -1, -1) // index row for alien in every col. -1 = no alien
    var spaceshipPosition = 1  // middle=1 , right=0, left=2
    private var lives = 3

    fun newAlien(): Int {
        var col=0
        do{
            col = (0..2).random()
        } while(alienPositions[col] > -1)
        alienPositions[col]++
        return col
    }


    fun dropAlien(col: Int): Int {
        val row = alienPositions[col]
        if (row in 0..6) {
            alienPositions[col]++
        } else if (row == 7) {
            alienPositions[col] = -1
        }
        return row
    }

    fun moveLeft() {
        if (spaceshipPosition > 0) {
            spaceshipPosition--
        }
    }

    fun moveRight() {
        if (spaceshipPosition < 2) {
            spaceshipPosition++
        }
    }


    fun crash(context: Context): Int{
        SignalManager.vibrate(context)
        lives--
        if(lives==0) loseGame(context)
        else SignalManager.toast(context,"Crash!")
        return lives
    }

    fun loseGame(context: Context){
        SignalManager.toast(context,"Crash! Lose Game!!")
        lives = 3
        spaceshipPosition = 1
    }
}