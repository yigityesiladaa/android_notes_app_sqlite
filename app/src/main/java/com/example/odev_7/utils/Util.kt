package com.example.odev_7.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.odev_7.components.AddNotePopUpFragment
import com.example.odev_7.interfaces.IHomeListener


object Util {

    fun Fragment.showToast(message : String){
        Toast.makeText(this.context,message,Toast.LENGTH_LONG).show()
    }

    //it will be edited later and updated according to the dependency injection structure
    fun Fragment.showAddNotePopUp(listener : IHomeListener){
        val fragment = AddNotePopUpFragment()
        childFragmentManager.beginTransaction().remove(fragment).commit()
        fragment.setListener(listener)
        fragment?.show(childFragmentManager,AddNotePopUpFragment.TAG)
    }

}