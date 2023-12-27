package kr.esei.library.model

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

public data class ActivityResultEventModel(
    public val resultCode: Int,
    public val data: Bundle? = null
)
