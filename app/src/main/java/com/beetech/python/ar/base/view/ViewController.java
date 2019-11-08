package com.beetech.python.ar.base.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.beetech.python.ar.R;
import com.beetech.python.ar.view.impl.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewController<T extends BaseFragment> {

    private int layoutId;
    private FragmentManager fragmentManager;
    private List<T> listAddFragment;
    //private List<Class<T>> listClass;
    private T currentFragment = null;

    private OnFragmentChangedListener onFragmentChangedListener;

    /**
     * Constructor
     *
     * @param fragmentManager FragmentManager
     * @param layoutId        resource layout id of fragment
     */
    public ViewController(FragmentManager fragmentManager, int layoutId) {
        this.fragmentManager = fragmentManager;
        this.layoutId = layoutId;
        listAddFragment = new ArrayList<>();
        //listClass = new ArrayList<>();
    }

    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

//    public void replaceFragment(BaseFragment fragment, Map<String, Object> data) {
//        if (!listClass.isEmpty() && getCurrentFragment().getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
//            return;
//        }
//        if (data != null) {
//            fragment.setData(data);
//        }
//        fragment.setViewController(this);
//        fragmentManager.beginTransaction().replace(layoutId, fragment).commit();
//        listClass.add(fragment);
//    }

    public void replaceFragment(Class<T> type, HashMap<String, Object> data) {
        /*
        // Comment: open two notification at the same time, will open two instances of a fragment
        if (currentFragment != null && currentFragment.getClass().getName().equalsIgnoreCase(type.getName())) {
            return;
        }*/
        try {
            currentFragment = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (data != null) {
            currentFragment.setData(data);
        }
        currentFragment.setViewController(this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out,
//                R.anim.trans_right_in, R.anim.trans_right_out);
        fragmentTransaction.replace(layoutId, currentFragment).commitAllowingStateLoss();
        listAddFragment.remove(listAddFragment.size() - 1);
        listAddFragment.add(currentFragment);
        if (onFragmentChangedListener != null) {
            onFragmentChangedListener.onFragmentChanged();
        }
    }

    public void addFragment(Class<T> type, HashMap<String, Object> data, boolean hasAnimation, boolean isHideOldFragment) {
        /*
        // Comment: open two notification at the same time, will open two instances of a fragment
        if (currentFragment != null && currentFragment.getClass().getName().equalsIgnoreCase(type.getName())) {
            return;
        }*/
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        if (hasAnimation) {
//            fragmentTransaction.setCustomAnimations(R.anim.trans_right_in, R.anim.trans_right_in);
//        } else {
//            fragmentTransaction.setCustomAnimations(R.anim.animation_none, R.anim.animation_none);
//        }
        T newFragment = null;
        try {
            newFragment = type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newFragment != null && data != null) {
            newFragment.setData(data);
        }
        if (newFragment != null) {
            newFragment.setViewController(this);
            fragmentTransaction.add(layoutId, newFragment, type.getSimpleName());
            if (currentFragment != null) {
//                if (hasAnimation) {
//                    fragmentTransaction.setCustomAnimations(R.anim.animation_in_delay, R.anim.animation_in_delay);
//                } else {
//                    fragmentTransaction.setCustomAnimations(R.anim.animation_none, R.anim.animation_none);
//                }
                currentFragment.setUserVisibleHint(false);
                if (isHideOldFragment) {
                    fragmentTransaction.hide(currentFragment);
                }
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
        listAddFragment.add(newFragment);
        currentFragment = newFragment;
        if (onFragmentChangedListener != null) {
            onFragmentChangedListener.onFragmentChanged();
        }
    }

    public void addFragmentUpAnimation(Class<T> type, HashMap<String, Object> data, boolean hasAnimation) {
        /*
        // Comment: open two notification at the same time, will open two instances of a fragment
        if (currentFragment != null && currentFragment.getClass().getName().equalsIgnoreCase(type.getName())) {
            return;
        }*/
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        if (hasAnimation) {
//            fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_up);
//        } else {
//            fragmentTransaction.setCustomAnimations(R.anim.animation_none, R.anim.animation_none);
//        }
        T newFragment = null;
        try {
            newFragment = type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newFragment != null && data != null) {
            newFragment.setData(data);
        }
        if (newFragment != null) {
            newFragment.setViewController(this);
            fragmentTransaction.add(layoutId, newFragment, type.getSimpleName());
            if (currentFragment != null) {
//                if (hasAnimation) {
//                    fragmentTransaction.setCustomAnimations(R.anim.animation_in_delay, R.anim.animation_in_delay);
//                } else {
//                    fragmentTransaction.setCustomAnimations(R.anim.animation_none, R.anim.animation_none);
//                }
                fragmentTransaction.hide(currentFragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
        listAddFragment.add(newFragment);
        currentFragment = newFragment;
        if (onFragmentChangedListener != null) {
            onFragmentChangedListener.onFragmentChanged();
        }
    }

    public void addFragmentUpAnimation(Class<T> type, HashMap<String, Object> data) {
        addFragmentUpAnimation(type, data, true);
    }

    public void addFragment(Class<T> type, HashMap<String, Object> data) {
        addFragment(type, data, true, true);
    }

    public boolean backFromAddFragment(HashMap<String, Object> data) {
        if (listAddFragment.size() >= 2) {
            listAddFragment.remove(listAddFragment.size() - 1);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(currentFragment);
//            fragmentTransaction.setCustomAnimations(R.anim.trans_right_out, R.anim.trans_right_out);
            currentFragment = listAddFragment.get(listAddFragment.size() - 1);
            if (data != null) {
                currentFragment.setData(data);
            }
            currentFragment.setViewController(this);
            currentFragment.setUserVisibleHint(true);
//            fragmentTransaction.setCustomAnimations(R.anim.animation_none, R.anim.animation_none);
            fragmentTransaction.show(currentFragment);
            fragmentTransaction.commitAllowingStateLoss();
            currentFragment.backFromAddFragment();
            if (onFragmentChangedListener != null) {
                onFragmentChangedListener.onFragmentChanged();
            }
            currentFragment.hideLayoutRetry();
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAllFragmentExceptFirst(HashMap<String, Object> data) {
        if (listAddFragment.size() >= 2) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (int i = listAddFragment.size() - 1; i > 0; i--) {
                fragmentTransaction.remove(listAddFragment.get(i));
            }
            currentFragment = listAddFragment.get(0);
            listAddFragment.clear();
            listAddFragment.add(currentFragment);

            if (data != null) {
                currentFragment.setData(data);
            }
            currentFragment.setViewController(this);
            currentFragment.setUserVisibleHint(true);
            fragmentTransaction.show(currentFragment);
            fragmentTransaction.commitAllowingStateLoss();
            currentFragment.backFromAddFragment();
            if (onFragmentChangedListener != null) {
                onFragmentChangedListener.onFragmentChanged();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean backFromAddFragmentDownAnimation(HashMap<String, Object> data) {
        if (listAddFragment.size() >= 2) {

            listAddFragment.remove(listAddFragment.size() - 1);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(currentFragment);
//            fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_down);
            currentFragment = listAddFragment.get(listAddFragment.size() - 1);
            if (data != null) {
                currentFragment.setData(data);
            }
            currentFragment.setViewController(this);
            currentFragment.setUserVisibleHint(true);
//            fragmentTransaction.setCustomAnimations(R.anim.animation_none, R.anim.animation_none);
            fragmentTransaction.show(currentFragment);
            fragmentTransaction.commitAllowingStateLoss();
            currentFragment.backFromAddFragment();
            if (onFragmentChangedListener != null) {
                onFragmentChangedListener.onFragmentChanged();
            }
            return true;
        } else {
            return false;
        }
    }

//    public boolean backFromReplaceFragment(HashMap<String, Object> data) {
//        if (listClass.size() >= 2) {
//            listClass.remove(listClass.size() - 1);
//            try {
//                currentFragment = listClass.get(listClass.size() - 1).newInstance();
//
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            if (data != null) {
//                currentFragment.setData(data);
//            }
//            currentFragment.setViewController(this);
//            listAddFragment.clear();
//            listAddFragment.add(currentFragment);
//            fragmentManager.beginTransaction().replace(layoutId, currentFragment).commit();
//            return true;
//        } else {
//            return false;
//        }
//    }

//    public void insertFragmentToList(BaseFragment fragment, Map<String, Object> data) {
//        if (!listClass.isEmpty() && getCurrentFragment().getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
//            return;
//        }
//        if (data != null) {
//            fragment.setData(data);
//        }
//        fragment.setViewController(this);
//        listClass.add(fragment);
//    }

//    public boolean backFromReplaceFragment(Map<String, Object> data) {
//        if (listClass.size() >= 2) {
//            listClass.remove(listClass.size() - 1);
//            BaseFragment fragment = listClass.get(listClass.size() - 1);
//            if (data != null) {
//                fragment.setData(data);
//            }
//            fragmentManager.beginTransaction().replace(layoutId, fragment).commit();
//            return true;
//        } else {
//            return false;
//        }
//    }

    public void clearAllFragment() {
        listAddFragment.clear();
    }

    public Fragment findFragmentByTag(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    public void setOnFragmentChangedListener(OnFragmentChangedListener onFragmentChangedListener) {
        this.onFragmentChangedListener = onFragmentChangedListener;
    }

    public interface OnFragmentChangedListener {
        void onFragmentChanged();
    }
}
