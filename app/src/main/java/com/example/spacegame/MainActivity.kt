package com.example.spacegame

import android.view.View
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.spacegame.com.example.spacegame.GameManager
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var leftArrow: ImageButton
    private lateinit var rightArrow: ImageButton
    private lateinit var spaceships: Array<ImageView>
    private lateinit var live1: ImageView
    private lateinit var live2: ImageView
    private lateinit var live3: ImageView
    private lateinit var aliens: Array<Array<ImageView>>
    private lateinit var moveAlienTimer: Timer
    private lateinit var newAlienTimer: Timer
    private lateinit var gameManager: GameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // XML part
        leftArrow = findViewById(R.id.leftArrow)
        rightArrow = findViewById(R.id.rightArrow)
        live1 = findViewById(R.id.live1)
        live2 = findViewById(R.id.live2)
        live3 = findViewById(R.id.live3)
        spaceships = arrayOf(findViewById(R.id.leftSpaceship), findViewById(R.id.middleSpaceship), findViewById(R.id.rightSpaceship))
        aliens = arrayOf(   // matrix for aliens
            arrayOf(findViewById(R.id.alien01), findViewById(R.id.alien02), findViewById(R.id.alien03)),
            arrayOf(findViewById(R.id.alien11), findViewById(R.id.alien12), findViewById(R.id.alien13)),
            arrayOf(findViewById(R.id.alien21), findViewById(R.id.alien22), findViewById(R.id.alien23)),
            arrayOf(findViewById(R.id.alien31), findViewById(R.id.alien32), findViewById(R.id.alien33)),
            arrayOf(findViewById(R.id.alien41), findViewById(R.id.alien42), findViewById(R.id.alien43)),
            arrayOf(findViewById(R.id.alien51), findViewById(R.id.alien52), findViewById(R.id.alien53)),
            arrayOf(findViewById(R.id.alien61), findViewById(R.id.alien62), findViewById(R.id.alien63)),
            arrayOf(findViewById(R.id.alien71), findViewById(R.id.alien72), findViewById(R.id.alien73))
        )
        gameManager = GameManager()
        inivisibleAliens()
        updateSpaceshipVisibility()
        clickOnArrows()
        alienTimer()
    }


    private fun alienTimer() {
        newAlienTimer = Timer()
        newAlienTimer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    newAlien()
                }
            }
        }, 0L, Constants.Timer.NEW_ALIEN)

        moveAlienTimer = Timer()
        moveAlienTimer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    dropAlien()
                }
            }
        }, 0L, Constants.Timer.DROP_ALIEN)
    }

    private fun clickOnArrows() {
        rightArrow.setOnClickListener {
            gameManager.moveRight()
            updateSpaceshipVisibility()
        }

        leftArrow.setOnClickListener {
            gameManager.moveLeft()
            updateSpaceshipVisibility()
        }
    }

    private fun inivisibleAliens() {
        for (row in 0..< Constants.Alien.ROWS) {
            for (col in 0..< Constants.Alien.COLS) {
                aliens[row][col].visibility = View.INVISIBLE
            }
        }
    }

    private fun updateSpaceshipVisibility() {
        for ((index, spaceship) in spaceships.withIndex()) {
            spaceship.visibility = if (index == gameManager.spaceshipPosition) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun newAlien() {
       val col = gameManager.newAlien()
       aliens[0][col].visibility = View.VISIBLE
    }

    private fun dropAlien() {
        var row= 0
        for (col in 0..< Constants.Alien.COLS) {
            row = gameManager.dropAlien(col)
            if (row == -1) { // no alien
                continue
            }
            if (row < 7) {          // moving down the alien
                aliens[row][col].visibility = View.INVISIBLE
                aliens[row + 1][col].visibility = View.VISIBLE

            } else if (row == 7) {  // last row, checking crash
                if (gameManager.spaceshipPosition == col) crash()
                aliens[row][col].visibility = View.INVISIBLE
            }
        }
    }

    private fun crash(){
        val lives = gameManager.crash(this)
        if (lives == 2) live3.visibility = View.INVISIBLE
        else if(lives == 1) live2.visibility = View.INVISIBLE
        else if(lives == 3) restartGame()
    }

    private fun restartGame(){
        gameManager = GameManager()  // restart everything
        inivisibleAliens()
        updateSpaceshipVisibility()
        live1.visibility = View.VISIBLE
        live2.visibility = View.VISIBLE
        live3.visibility = View.VISIBLE
    }

}




