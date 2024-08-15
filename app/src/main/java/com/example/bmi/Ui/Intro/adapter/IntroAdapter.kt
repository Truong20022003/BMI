package com.example.bmi.Ui.Intro.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bmi.R
import com.example.bmi.Ui.Intro.Model.IntroModel
import com.example.bmi.databinding.ItemIntroBinding

class IntroAdapter(val context: Context, var list: List<IntroModel>) : RecyclerView.Adapter<IntroAdapter.IntroViewHolder>()  {

    inner class IntroViewHolder(val binding: ItemIntroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: IntroModel, context: Context) {
            Glide.with(context).load(data.image).into(binding.imgIntro)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_intro, parent, false)
        return IntroViewHolder(ItemIntroBinding.bind(view))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        holder.bind(list[position], context)
    }

}