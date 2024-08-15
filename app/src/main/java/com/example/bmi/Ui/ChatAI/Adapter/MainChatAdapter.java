package com.example.bmi.Ui.ChatAI.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bmi.Ui.ChatAI.Fragments.ChatFragment;
import com.example.bmi.Ui.ChatAI.Fragments.HistoryFragment;


// Adapter for the main ViewPager2
public class MainChatAdapter extends FragmentStateAdapter {

    // Constructor
    public MainChatAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // Create the fragment based on position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new HistoryFragment();

            default:
                return null; // Return null for invalid position
        }
    }

    // Return the total number of fragments
    @Override
    public int getItemCount() {
        return 2;
    }
}

