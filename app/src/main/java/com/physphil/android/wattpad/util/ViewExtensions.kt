package com.physphil.android.wattpad.util

import android.view.View

/**
 * View-related extension methods
 * Copyright (c) 2017 Phil Shadlyn
 */

/**
 * Convenience method to set view's visibility. Set to View.VISIBLE if parameter is TRUE, set to
 * View.GONE if false.
 * @param visible Whether the view should be visible or not.
 */
fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
