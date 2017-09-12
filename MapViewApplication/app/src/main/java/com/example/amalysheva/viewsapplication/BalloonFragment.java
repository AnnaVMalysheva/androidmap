package com.example.amalysheva.viewsapplication;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BalloonFragment extends Fragment {
    private OnBalloonFragmentInteractionListener mListener;
    private static int myItemsCounter;
    BalloonsListAdapter myListAdapter;
    SortEnum sortEnum = SortEnum.BY_ID;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ballon, container );
        myListAdapter = new BalloonsListAdapter(getActivity(), new ArrayList<>());

        ListView balloonsList = (ListView) view.findViewById(R.id.lvMain);
        balloonsList.setAdapter(myListAdapter);

        balloonsList.setOnItemClickListener((parent, view12, position, id) -> {
            final MapBalloonItem myItem = myListAdapter.getItem(position);
            mListener.onShowBalloon(myItem.getBalloon_id());
        });

        balloonsList.setOnItemLongClickListener((parent, view1, position, id) -> {
            final TextView textView = (TextView) view1.findViewById(R.id.textAddress);
            PopupMenu popupMenu = new PopupMenu(BalloonFragment.this.getContext(), view1);
            popupMenu.inflate(R.menu.balloon_menu);
            final MapBalloonItem myItem = myListAdapter.getItem(position);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.change:
                          View layout = inflater.inflate(R.layout.balloon_dialog, container);
                          final EditText editText = (EditText) layout.findViewById(R.id.editText);
                          editText.setText(textView.getText().toString());
                          AlertDialog.Builder builder = new AlertDialog.Builder(BalloonFragment.this.getContext());
                          builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                              final String address = editText.getText().toString();
                              myItem.setAddress(address);
                              myListAdapter.sort(sortEnum);
                              myListAdapter.notifyDataSetChanged();
                              mListener.onChangeText(myItem.getBalloon_id(), address);
                              dialog.dismiss();
                          });
                          builder.setView(layout);
                          AlertDialog alertDialog = builder.create();
                          alertDialog.show();
                        return true;
                    case R.id.remove:
                        myListAdapter.remove(myItem, sortEnum);
                        myItemsCounter--;
                        mListener.onDeleteBalloon(myItem.getBalloon_id());
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
            return true;
        });

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radios);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId) {
                case R.id.byId:
                    sortEnum = SortEnum.BY_ID;
                    myListAdapter.sort(SortEnum.BY_ID);
                    break;
                case R.id.byName:
                    sortEnum = SortEnum.BY_NAME;
                    myListAdapter.sort(SortEnum.BY_NAME);
                    break;
            }
        });

        Button navButton = (Button) view.findViewById(R.id.navButton);
        navButton.setOnClickListener(v -> {
            Intent intent = new Intent("ru.yandex.yandexnavi.action.BUILD_ROUTE_ON_MAP");
            intent.setPackage("ru.yandex.yandexnavi");

            PackageManager pm = v.getContext().getPackageManager();
            List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

            if (infos == null || infos.size() == 0) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=ru.yandex.yandexnavi"));
            }

            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnBalloonFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }


    public void setText(final String text, Integer id) {
            getActivity().runOnUiThread(() -> {
                MapBalloonItem newItem = new MapBalloonItem(id, myItemsCounter++, text);
                myListAdapter.add(newItem, sortEnum);
            });
    }


    public interface OnBalloonFragmentInteractionListener {
        void onShowBalloon(Integer integer);
        void onChangeText(Integer integer, String text);
        void onDeleteBalloon(Integer integer);
    }
}
