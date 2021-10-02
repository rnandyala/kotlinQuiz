package com.nandyala.quizkotlinnandyala.model

import java.util.*

class QuizResult(
    var mDate: String?,
    var mScore: String?,
    var mUUID: String?
) {
    constructor() : this(null, null, null) {
    }
}