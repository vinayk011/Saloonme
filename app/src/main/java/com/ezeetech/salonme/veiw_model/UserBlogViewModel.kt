/*
 *  Created by Vinay on 2-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.veiw_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ezeetech.salonme.R
import com.ezeetech.salonme.model.UserBlog
import java.util.ArrayList

class UserBlogViewModel(val app: Application) : AndroidViewModel(app) {
    private val blogViewModel  = MutableLiveData<List<UserBlog>>()
    val _userBlogViewModel : LiveData<List<UserBlog>>
        get() = blogViewModel

    init {
       // init api network call
    }
    fun getUserBlogList(){
        blogViewModel.value = setDefaultData()
    }

    private fun setDefaultData() : List<UserBlog>{
        val blogList = ArrayList<UserBlog>()
        blogList.add(
            UserBlog(
                "1037",
                "https://picsum.photos/id/1037/400/250",
                app.getString(R.string.blog_title),
                app.getString(R.string.blog_content)
            )
        )
        blogList.add(
            UserBlog(
                "1073",
                "https://picsum.photos/id/1073/400/250",
                app.getString(R.string.blog_title),
                app.getString(R.string.blog_content)
            )
        )
        blogList.add(
            UserBlog(
                "119",
                "https://picsum.photos/id/119/400/250",
                app.getString(R.string.blog_title),
                app.getString(R.string.blog_content)
            )
        )
        blogList.add(
            UserBlog(
                "127",
                "https://picsum.photos/id/127/400/250",
                app.getString(R.string.blog_title),
                app.getString(R.string.blog_content)
            )
        )
        blogList.add(
            UserBlog(
                "133",
                "https://picsum.photos/id/133/400/250",
                app.getString(R.string.blog_title),
                app.getString(R.string.blog_content)
            )
        )
        blogList.add(
            UserBlog(
                "1023",
                "https://picsum.photos/id/1023/400/250",
                app.getString(R.string.blog_title),
                app.getString(R.string.blog_content)
            )
        )
        blogList.add(
            UserBlog(
                "1036",
                "https://picsum.photos/id/1036/400/250",
                app.getString(R.string.blog_title),
                app.getString(R.string.blog_content)
            )
        )

        return blogList
    }
}