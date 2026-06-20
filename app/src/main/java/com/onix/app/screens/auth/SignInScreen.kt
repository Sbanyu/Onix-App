package com.onix.app.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.components.LogoDisc
import com.onix.app.ui.components.OnixButton
import com.onix.app.ui.components.OnixField
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily

/**
 * Ported from screen_signin.jsx. "Sign In" originally routed to the lifestyle Home (out of
 * scope); here it goes to the thesis home instead, since that's our app's real landing screen.
 */
@Composable
fun SignInScreen(
    onSignIn: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("brian@onix.app") }
    var password by remember { mutableStateOf("••••••••") }
    var showPassword by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
        ) {
            Column(
                modifier = Modifier.padding(top = 36.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp),
            ) {
                LogoDisc(size = 72.dp, radius = 20.dp)
                Column {
                    Text(
                        text = "Welcome\nback, Brian",
                        color = OnixColors.Fg1,
                        fontFamily = EditorialFontFamily,
                        fontWeight = FontWeight.W300,
                        fontSize = 44.sp,
                        lineHeight = 46.sp,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Let's pick up where you left off. Your streak is waiting.",
                        color = OnixColors.Fg2,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 15.sp,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(top = 38.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OnixField(
                    value = email,
                    onValueChange = { email = it },
                    icon = "envelope",
                    placeholder = "Email",
                    keyboardType = KeyboardType.Email,
                )
                OnixField(
                    value = password,
                    onValueChange = { password = it },
                    icon = "lock",
                    placeholder = "Password",
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailing = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showPassword = !showPassword },
                            contentAlignment = Alignment.Center,
                        ) {
                            OnixIcon(name = "eye", size = 18.dp, color = OnixColors.Fg3)
                        }
                    },
                )
                Box(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                    Text(
                        text = "Forgot password?",
                        color = OnixColors.Red,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.CenterEnd),
                    )
                }
            }

            Box(modifier = Modifier.padding(top = 26.dp)) {
                OnixButton(text = "Sign In", onClick = onSignIn, icon = "arrowRight", fullWidth = true)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 26.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(modifier = Modifier.weight(1f).height(1.dp).background(OnixColors.Border1))
                Text(text = "or continue with", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 12.sp)
                Box(modifier = Modifier.weight(1f).height(1.dp).background(OnixColors.Border1))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                listOf("Apple", "Google", "Strava").forEach { provider ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(999.dp))
                            .background(OnixColors.BgSecondary)
                            .border(1.dp, OnixColors.Border2, RoundedCornerShape(999.dp))
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = provider,
                            color = OnixColors.Fg1,
                            fontFamily = SwitzerFontFamily,
                            fontWeight = FontWeight.W500,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = "New here? ", color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 14.sp)
            Text(
                text = "Create account",
                color = OnixColors.Red,
                fontFamily = SwitzerFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = 14.sp,
                modifier = Modifier.clickable(onClick = onCreateAccount),
            )
        }
    }
}
