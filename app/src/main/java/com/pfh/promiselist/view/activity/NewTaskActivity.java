package com.pfh.promiselist.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.pfh.promiselist.R;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.DensityUtil;
import com.pfh.promiselist.utils.UuidUtils;
import com.pfh.promiselist.widget.FlowLayout;
import com.pfh.promiselist.widget.ProjectChooseView;
import com.pfh.promiselist.widget.TagView;
import com.pfh.promiselist.widget.TagsChooseView;
import com.pfh.promiselist.widget.TaskPictureView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;


/**
 * 新建单条任务
 */

public class NewTaskActivity extends BaseActivity implements
        ColorChooserDialog.ColorCallback,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;

    private MaterialEditText et_title;
    private MaterialEditText et_desc;
    private FlowLayout flow_tag_container;
    private ImageView iv_back;
    private ImageView iv_palette;
    private ImageView iv_tag;
    private ImageView iv_fixed;
    private ImageView iv_date;
    private ImageView iv_ok;
    private Calendar targetCalendar;
    private ImageView iv_project;
    private TagView dateTag;
    private TagView projectTag;
    private ImageView iv_picture;
    private ImageView iv_camera;
    private PhotoView photoView;
    private LinearLayout ll_pic_container;

    private String colorValue;
    private Task task;
    private File mCurrentPhotoFile;
    private Info mRectF;

    private boolean canOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        findViews();
        initViews();
        String editTaskId = getIntent().getStringExtra(MainActivity.EDIT_TASK_ID);
        if (!TextUtils.isEmpty(editTaskId)){
            task = RealmDB.getTaskByTaskId(mRealm,editTaskId);
        }else {
            initData();
        }
        setupView();
    }

    private void findViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_palette = (ImageView) findViewById(R.id.iv_palette);
        iv_tag = (ImageView) findViewById(R.id.iv_tag);
        iv_fixed = (ImageView) findViewById(R.id.iv_fixed);
        iv_date = (ImageView) findViewById(R.id.iv_date);
        iv_project = (ImageView) findViewById(R.id.iv_project);
        iv_ok = (ImageView) findViewById(R.id.iv_ok);
        et_title = (MaterialEditText) findViewById(R.id.et_title);
        et_desc = (MaterialEditText) findViewById(R.id.et_desc);
        flow_tag_container = (FlowLayout) findViewById(R.id.flow_tag_container);
        flow_tag_container.setHorizontalSpacing(DensityUtil.dp2px(this,5));
        flow_tag_container.setVerticalSpacing(DensityUtil.dp2px(this,4));

        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);

        photoView = (PhotoView) findViewById(R.id.photoView);
        photoView.enable();
        photoView.setAnimaDuring(500);
        ll_pic_container = (LinearLayout) findViewById(R.id.ll_pic_container);

    }

    private void initViews() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });

        iv_palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorChooseDialog();
            }
        });

        iv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagChooseDialog();
            }
        });

        iv_fixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setFixed(!task.isFixed());
                iv_fixed.setImageResource(task.isFixed()? R.drawable.ic_fixed_blue : R.drawable.ic_fixed);
            }
        });

        iv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        iv_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectChooseDialog();

            }
        });

        iv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canOk) {
                    saveTask();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoView.animaTo(mRectF, new Runnable() {
                    @Override
                    public void run() {
                        photoView.setVisibility(View.GONE);
                    }
                });
            }
        });

        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    iv_ok.setImageResource(R.drawable.ic_send);
                    canOk = false;
                }else {
                    iv_ok.setImageResource(R.drawable.ic_send_blue );
                    canOk = true;
                }
            }
        });
    }


    private void initData() {
        task = new Task();
        task.setOwner(RealmDB.getUserByUserId(mRealm,RealmDB.getCurrentUserId()));
        task.setTaskId(UuidUtils.getShortUuid());
        task.setState(1);
        task.setCreatedTime(new Date().getTime());
        task.setTags(new RealmList<Tag>());//后面防空！
        targetCalendar = Calendar.getInstance();
        targetCalendar.set(Calendar.DAY_OF_MONTH,targetCalendar.get(Calendar.DAY_OF_MONTH)+1);
        targetCalendar.set(Calendar.HOUR_OF_DAY,9);
        targetCalendar.set(Calendar.MINUTE,0);
        targetCalendar.set(Calendar.SECOND,0);
        targetCalendar.set(Calendar.MILLISECOND,0);
        task.setDueTime(targetCalendar.getTimeInMillis());
        //默认所属清单
        task.setProject(RealmDB.getProjectByProjectName(mRealm,getString(R.string.collection_box)));
    }

    private void setupView(){
        if (task.isFixed()) {
            iv_fixed.setImageResource(R.drawable.ic_fixed_blue );
        }
        if (task.getName() != null) {
            et_title.setText(task.getName());
        }
        if (task.getDesc() != null) {
            et_desc.setText(task.getDesc());
        }
        //时间tag
        dateTag = new TagView(mContext);
        dateTag.setData(TagView.TIME_TYPE,getStrByTime(task.getDueTime()));
        dateTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        flow_tag_container.addView(dateTag);
        //清单tag
        projectTag = new TagView(mContext);
        projectTag.setData(TagView.PROJECT_TYPE, task.getProject().getName());
        projectTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectChooseDialog();
            }
        });
        flow_tag_container.addView(projectTag);
        //标签tag
        addTagView();
        // 图片 TODO
        addPic();
    }

    private void saveTask() {
        task.setName(et_title.getText().toString());
        task.setDesc(et_desc.getText().toString());
        // 图片 TODO
        task.setFixed(task.isFixed());
        RealmDB.saveTaskToProject(mRealm,task.getProject().getProjectId(),task);
    }

    private void addPic(){
        ll_pic_container.removeAllViews();
        if (!TextUtils.isEmpty(task.getPicUrls())) {
            String[] pics = task.getPicUrls().split(",");
            for (int i = 0; i < pics.length; i++) {
                final TaskPictureView taskPictureView = new TaskPictureView(mContext);
                taskPictureView.setPicUrl(pics[i]);
                taskPictureView.getPicView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Glide.with(mContext)
                                .load(taskPictureView.getPicUrl())
                                .into(photoView);
                        photoView.setVisibility(View.VISIBLE);
                        mRectF = taskPictureView.getPicView().getInfo();
                        photoView.animaFrom(mRectF);
                    }
                });

                taskPictureView.getIvDelete().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] pics = task.getPicUrls().split(",");
                        String newPicUrls = "";
                        int index = -1;
                        for (int j = 0; j < pics.length; j++) {
                            if (pics[j].equals(taskPictureView.getPicUrl())) {
                                index = j;
                            }else {
                                newPicUrls += newPicUrls.equals("") ? pics[j] : ","+pics[j];
                            }
                        }
                        if (index != -1){
                            ll_pic_container.removeViewAt(index);
                            task.setPicUrls(newPicUrls);
                        }
                    }
                });

                ll_pic_container.addView(taskPictureView);
            }
        }
    }

    private void addTagView(){
        RealmList<Tag> tags = task.getTags();
        for (int i = 0; i < tags.size(); i++) {
            TagView tag_tag = new TagView(mContext);
            Tag tag = tags.get(i);
            tag_tag.setData(TagView.TAG_TYPE,tag.getName());
            flow_tag_container.addView(tag_tag);
        }
    }

    private void showColorChooseDialog() {
        String[] colorArrays = getResources().getStringArray(R.array.colors);
        int[] colors = new int[colorArrays.length];
        for (int i = 0; i < colorArrays.length; i++) {
            colors[i] = Color.parseColor(colorArrays[i]);
        }
        new ColorChooserDialog.Builder(this,R.string.choose_color)
                .customColors(colors,null)
                .accentMode(false)
                .allowUserColorInput(false)
                .doneButton(R.string.ok)
                .cancelButton(R.string.cancel)
                .show();
    }

    private void showTagChooseDialog(){
        List<Tag> allTags = RealmDB.getAllTagsByUserId(mRealm, RealmDB.getCurrentUserId());
        final TagsChooseView tagsChooseView = new TagsChooseView(mContext);
        List<Task> tempList = new ArrayList<>();
        tempList.add(task);
        tagsChooseView.setData(allTags,tempList,mRealm);
        new MaterialDialog.Builder(mContext)
                .title(R.string.add_tag)
                .customView(tagsChooseView,true)
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        tagsChooseView.onPositive();
                        flow_tag_container.removeAllViews();
                        flow_tag_container.addView(dateTag);
                        flow_tag_container.addView(projectTag);
                        addTagView();
                    }
                })
                .show();
    }

    private void showProjectChooseDialog() {
        List<Project> allProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
        ProjectChooseView projectChooseView = new ProjectChooseView(mContext);
        List<Task> tempList = new ArrayList<>();
        tempList.add(task);
        projectChooseView.setData(allProjects,tempList,mRealm);

        final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(R.string.project_choose_dialog_label)
                .customView(projectChooseView, true)
                .negativeText(R.string.cancel)
                .show();

        projectChooseView.setOnProjectSelectedListener(new ProjectChooseView.onProjectSelectedListener() {
            @Override
            public void projectSelected() {
                dialog.dismiss();
                projectTag.setData(TagView.PROJECT_TYPE,task.getProject().getName());
            }
        });

    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                NewTaskActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(Color.parseColor("#6EC9FD"));
        dpd.setTitle(getString(R.string.set_due_time));
        dpd.setMinDate(now);
        dpd.show(getFragmentManager(),"tag");
    }

    private void showTimePickerDialog(){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                NewTaskActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setAccentColor(Color.parseColor("#6EC9FD"));
        tpd.setTitle(getString(R.string.set_due_time));
        tpd.show(getFragmentManager(),"tag");
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        String color_hex = String.format("#%06X", (0xFFFFFF & selectedColor));
        colorValue = color_hex;
//        et_title.setBaseColor(selectedColor);
        et_title.setUnderlineColor(selectedColor);
        et_title.setPrimaryColor(selectedColor);
        task.setColorValue(colorValue);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Log.e("TAg","year: "+year + " monthOfYear: "+monthOfYear + " dayOfMonth: "+dayOfMonth);
        showTimePickerDialog();
        targetCalendar.set(Calendar.YEAR,year);
        targetCalendar.set(Calendar.MONTH,monthOfYear);
        targetCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        dateTag.setData(TagView.TIME_TYPE,getStrByTime(targetCalendar.getTimeInMillis()));
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Log.e("TAg","hourOfDay: "+hourOfDay + " minute: "+minute + " second: "+second);
        targetCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        targetCalendar.set(Calendar.MINUTE,minute);
        task.setDueTime(targetCalendar.getTimeInMillis());
        dateTag.setData(TagView.TIME_TYPE,getStrByTime(targetCalendar.getTimeInMillis()));
    }

    private String getStrByTime(long time){
        if (DateUtil.isToday(time)){
            return "今天 "+DateUtil.timeStamp2Str(time,"HH:mm"); // 今天 10:00
        }else if (DateUtil.isTomorrow(time)){
            return "明天 "+DateUtil.timeStamp2Str(time,"HH:mm"); // 明天 10:00
        }else {
            return DateUtil.timeStamp2Str(time,"MM-dd HH:mm"); // 12-18 11:00
        }
    }

    @Override
    public void onBackPressed() {
        if (photoView.getVisibility() == View.VISIBLE){
            photoView.animaTo(mRectF, new Runnable() {
                @Override
                public void run() {
                    photoView.setVisibility(View.GONE);
                }
            });
        }else {
            showConfirmDialog();
        }
    }

    private void showConfirmDialog() {
        if (!TextUtils.isEmpty(et_title.getText())){
            new MaterialDialog.Builder(this)
                    .content("丢弃您的更改吗")
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    })
                    .show();
        }else {
            finish();
        }
    }

    protected void openAlbum(){
        // 打开系统相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    private void openCamera(){
        try {
            File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
            PHOTO_DIR.mkdirs();
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        }catch (Exception e) {
            Log.e("TAG","相机不可用");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String picUrl = "";

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            picUrl= getRealFilePath(uri);
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            picUrl = mCurrentPhotoFile.getAbsolutePath();
        }
        task.setPicUrls(TextUtils.isEmpty(task.getPicUrls())? picUrl : task.getPicUrls()+","+picUrl);
        addPic();
    }

}
