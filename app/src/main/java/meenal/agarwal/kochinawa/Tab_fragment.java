package meenal.agarwal.kochinawa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Tab_fragment extends FragmentPagerAdapter
{

    public Tab_fragment(@NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {switch(position)
        {
            case 0:
                GroupChat_Fragment groupFragment=new GroupChat_Fragment();
                return groupFragment;
            case 1:
               MainChat_Fragment MainFragment=new   MainChat_Fragment();
                return MainFragment;
            case 2:
               Contact_Fragment ContactFragment=new  Contact_Fragment();
                return ContactFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
        case 0:

        return "groups";
        case 1:

        return"Main Chats";
        case 2:

        return "Contact";
        default:
        return null;
    }
}
}
