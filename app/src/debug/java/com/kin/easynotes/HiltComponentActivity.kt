package com.kin.easynotes

import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

//https://github.com/google/dagger/issues/3394#issuecomment-2432661970
//https://stackoverflow.com/a/79656862/17464278
//check this issue so that we make empty activity for hilt and further we add our content in test
@AndroidEntryPoint
class HiltComponentActivity: ComponentActivity()