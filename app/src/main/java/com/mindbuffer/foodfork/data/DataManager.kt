package com.mindbuffer.foodfork.data

import com.mindbuffer.foodfork.data.local.db.DbHelper
import com.mindbuffer.foodfork.data.remote.ApiHelper

interface DataManager: ApiHelper, DbHelper {
}