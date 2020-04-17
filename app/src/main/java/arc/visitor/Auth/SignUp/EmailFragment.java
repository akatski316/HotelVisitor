package arc.visitor.Auth.SignUp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import arc.visitor.R;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class EmailFragment extends Fragment {

    EditText name,email, password,confirmpassword,phone;

    private static final String TAG = "EmailFragment";

    //..miscelleneous
    private AwesomeValidation mAwesomeValidation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        password = view.findViewById(R.id.password);
        confirmpassword = view.findViewById(R.id.confirm_password);
        phone = view.findViewById(R.id.phone);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAwesomeValidation = new AwesomeValidation(BASIC);
        addValidationForEditText(getActivity());
    }

    public boolean submit(View v)
    {
        Log.d(TAG, "submit: ");
        if(!password.getText().toString().equals(confirmpassword.getText().toString()) && !password.getText().toString().equals("")){
            Toast.makeText(getContext(),"passwords don't match",Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean valid = mAwesomeValidation.validate();
        if(valid) {

            ((SignUpActivity)getActivity()).email_text = email.getText().toString();
            ((SignUpActivity)getActivity()).password_text = password.getText().toString();
            ((SignUpActivity)getActivity()).name = name.getText().toString();
            ((SignUpActivity)getActivity()).phone = phone.getText().toString();
            mAwesomeValidation.clear();
            return true;
        }
        return false;
    }

    private void addValidationForEditText(Activity activity)
    {
        Log.d(TAG, "addValidationForEditText: activity "+activity);
        mAwesomeValidation.addValidation(activity, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        mAwesomeValidation.addValidation(activity,R.id.phone, Patterns.PHONE,R.string.err_phone);
        String regexPassword = "/^.{6,}$/";
        mAwesomeValidation.addValidation(activity,R.id.password,".+",R.string.err_password);
        mAwesomeValidation.addValidation(activity,R.id.name, "[a-zA-Z\\s]+",R.string.err_name);
    }
}
