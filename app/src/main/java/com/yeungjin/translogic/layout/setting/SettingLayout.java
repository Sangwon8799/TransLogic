package com.yeungjin.translogic.layout.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonFragment;
import com.yeungjin.translogic.layout.login.LoginLayout;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.server.Server;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.utility.Image;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;
import java.util.Objects;

public class SettingLayout extends CommonFragment {
    private ImageView profile_image;
    private ImageButton edit;
    private TextView name;
    private TextView username;
    private TextView company;
    private TextView contact_number;
    private TextView email;
    private LinearLayout logout;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public Listener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_setting_setting, container, false);
        init();

        Glide.with(profile_image.getContext()).load(Server.IMAGE_URL + Session.USER.EMPLOYEE_IMAGE).into(profile_image);
        name.setText(Session.USER.EMPLOYEE_NAME);
        username.setText(Session.USER.EMPLOYEE_USERNAME);
        company.setText(Session.USER.EMPLOYEE_COMPANY_NAME);
        contact_number.setText(ContactNumber.parse(Session.USER.EMPLOYEE_CONTACT_NUMBER));
        email.setText(Session.USER.EMPLOYEE_EMAIL);

        profile_image.setClipToOutline(true);

        return view;
    }

    @Override
    protected void setId() {
        profile_image = view.findViewById(R.id.layout_setting_setting__image);
        edit = view.findViewById(R.id.layout_setting_setting__edit);
        name = view.findViewById(R.id.layout_setting_setting__name);
        username = view.findViewById(R.id.layout_setting_setting__username);
        company = view.findViewById(R.id.layout_setting_setting__company);
        contact_number = view.findViewById(R.id.layout_setting_setting__contact_number);
        email = view.findViewById(R.id.layout_setting_setting__email);
        logout = view.findViewById(R.id.layout_setting_setting__logout);
    }

    @Override
    protected void setListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginLayout.class);
                intent.putExtra("logout", true);
                startActivity(intent);

                Objects.requireNonNull(listener).exit();
            }
        });
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    Bitmap image = null;

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            image = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().getContentResolver(), uri));
                        } else {
                            image = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                        }
                    } catch (Exception error) {
                        error.printStackTrace();
                    }

                    if (image != null) {
                        int width = image.getWidth();
                        int height = image.getHeight();
                        int value = Math.min(width, height);

                        Bitmap bitmap;
                        if (width > height) {
                            bitmap = Bitmap.createBitmap(image, (width - height) / 2, 0, value, value);
                        } else {
                            bitmap = Bitmap.createBitmap(image, 0, (height - width) / 2, value, value);
                        }
                        Bitmap resized = Bitmap.createScaledBitmap(Objects.requireNonNull(bitmap), Image.Ratio.MAX_SIZE, Image.Ratio.MAX_SIZE, true);
                        profile_image.setImageBitmap(resized);

                        new DBThread("SetEmployeeImage", new HashMap<String, Object>() {{
                            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                            put("base64", Image.toBase64(resized));
                        }});
                    }
                }
            }
        });
    }

    public interface Listener {
        void exit();
    }
}
