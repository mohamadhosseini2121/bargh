package com.example.bargh.adapter;

import com.example.bargh.view.fragment.ProductsFragment;
import com.example.bargh.view.fragment.PurchasesFragment;
import com.example.bargh.view.fragment.ServicesFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> arrayList = new ArrayList<>();

    public MainPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
                            RequestedServicesAdapter.OnRequestedServicesListener onRequestedServicesListener) {
        super(fragmentManager, lifecycle);
        arrayList.add(new ServicesFragment(onRequestedServicesListener));
        arrayList.add(new ProductsFragment());
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return arrayList.get(position);
    }

    public Fragment getFragment (int position){
        return arrayList.get(position);
    }

    public void addFragment(Fragment fragment) {
        arrayList.add(fragment);
        notifyDataSetChanged();
    }

    public void removeFragment(int index){
        arrayList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
