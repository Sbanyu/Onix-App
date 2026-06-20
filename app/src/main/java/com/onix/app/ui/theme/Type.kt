package com.onix.app.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.onix.app.R

/** PP Editorial Old — display serif. Weight 300 in the CSS is an alias of the same Ultralight file as 200. */
val EditorialFontFamily = FontFamily(
    Font(R.font.pp_editorial_ultralight, FontWeight.W200),
    Font(R.font.pp_editorial_ultralight, FontWeight.W300),
    Font(R.font.pp_editorial_regular, FontWeight.W400),
    Font(R.font.pp_editorial_ultrabold, FontWeight.W700),
)

/** Switzer — UI sans. */
val SwitzerFontFamily = FontFamily(
    Font(R.font.switzer_light, FontWeight.W300),
    Font(R.font.switzer_regular, FontWeight.W400),
    Font(R.font.switzer_medium, FontWeight.W500),
    Font(R.font.switzer_semibold, FontWeight.W600),
    Font(R.font.switzer_bold, FontWeight.W700),
)
