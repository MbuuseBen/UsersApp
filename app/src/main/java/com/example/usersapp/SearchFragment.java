//package com.example.usersapp;
//
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.speech.RecognizerIntent;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the  factory method to
// * create an instance of this fragment.
// */
//public class SearchFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public class SearchProductFragment extends Fragment {
//
//        private static final int REQ_SCAN_RESULT = 200;
//        private final int REQ_CODE_SPEECH_INPUT = 100;
//        ArrayList<Product> searchProductList = new ArrayList<>();
//        boolean searchInProgress = false;
//        private TextView heading;
//        private ImageButton btnSpeak;
//        private EditText serchInput;
//        private ListView serachListView;
//
//        /** The search adapter. */
//        // private SearchListArrayAdapter searchAdapter;
//        /**
//         * The root view.
//         */
//        private View rootView;
//
//        public static Fragment newInstance() {
//            // TODO Auto-generated method stub
//            return new SearchFragment();
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
//            rootView = inflater.inflate(R.layout.fragment_search,
//                    container, false);
//
//            btnSpeak = (ImageButton) rootView.findViewById(R.id.btnSpeak);
//
//            heading = (TextView) rootView.findViewById(R.id.txtSpeech_heading);
//
//            serchInput = (EditText) rootView.findViewById(R.id.edt_search_input);
//
//            serchInput.setSelected(true);
//
//            serachListView = (ListView) rootView
//                    .findViewById(R.id.search_list_view);
//
//            serachListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//
//                    Toast.makeText(getActivity(), "Selected" + position, 500)
//                            .show();
//
//                }
//            });
//
//            serchInput.addTextChangedListener(new TextWatcher() {
//
//                @Override
//                public void onTextChanged(CharSequence inputString, int arg1,
//                                          int arg2, int arg3) {
//
//                    heading.setText("Showing results for "
//                            + inputString.toString().toLowerCase());
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence arg0, int arg1,
//                                              int arg2, int arg3) {
//
//                    heading.setText("Search Products");
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable arg0) {
//                }
//            });
//            btnSpeak.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    promptSpeechInput();
//                }
//            });
//
//            rootView.setFocusableInTouchMode(true);
//            rootView.requestFocus();
//            rootView.setOnKeyListener(new View.OnKeyListener() {
//
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                    if (event.getAction() == KeyEvent.ACTION_UP
//                            && keyCode == KeyEvent.KEYCODE_BACK) {
//
//                        Utils.switchContent(R.id.frag_container,
//                                Utils.HOME_FRAGMENT,
//                                ((ECartHomeActivity) (getContext())),
//                                AnimationType.SLIDE_DOWN);
//                    }
//                    return true;
//                }
//            });
//            return rootView;
//
//        }
//
//        /**
//         * Showing google speech input dialog.
//         */
//        private void promptSpeechInput() {
//            searchInProgress = true;
//            searchProductList.clear();
//
//            heading.setText("Search Products");
//
//            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What do you wish for");
//            try {
//                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//            } catch (ActivityNotFoundException a) {
//                Toast.makeText(getActivity(),
//                        "Voice search not supported by your device ",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        /**
//         * Receiving speech input.
//         *
//         * @param requestCode the request code
//         * @param resultCode  the result code
//         * @param data        the data
//         */
//        @Override
//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//
//            searchInProgress = false;
//
//            if (resultCode == getActivity().RESULT_OK && null != data) {
//                switch (requestCode) {
//                    case REQ_CODE_SPEECH_INPUT: {
//
//                        ArrayList<String> result = data
//                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//
//                        heading.setText("Showing Results for " + result.get(0));
//
//                        break;
//                    }
//
//                    case REQ_SCAN_RESULT:
//                        //
//                        // String contents = data.getStringExtra("SCAN_RESULT");
//                        // String format = data.getStringExtra("SCAN_RESULT_FORMAT");
//                        // Toast.makeText(getActivity(), "Scan Success", 1000).show();
//                        break;
//
//                }
//
//            }
//
//        }
//
//    }
//}