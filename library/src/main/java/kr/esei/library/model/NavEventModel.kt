package kr.esei.library.model

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavOptions

public data class NavEventModel (
    @IdRes public val destinationId : Int,
    public val bundle: Bundle? = null,
    public val navOptions: NavOptions? = null
)