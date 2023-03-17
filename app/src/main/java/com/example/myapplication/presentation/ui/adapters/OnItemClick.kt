package com.example.myapplication.presentation.ui.adapters

import com.example.myapplication.data.local.pojo.User

/**
 * the reason i still like this approach is that it has more flexibility and control
 * over each child item that will be exposed to the action (onClick in this case)
 *
 * for now, since i only have one child i will use another approach.
 * **/
interface OnItemClick {

    fun onCardClick(position: Int): User
}