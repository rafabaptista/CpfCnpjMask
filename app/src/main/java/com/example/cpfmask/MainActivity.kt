package com.example.cpfmask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.cpfmask.helper.Mask
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edit_text_document?.addTextChangedListener(Mask.mask(edit_text_document))
    }
}
