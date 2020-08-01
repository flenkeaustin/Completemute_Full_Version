package com.example.completemute;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;
import com.zqg.dialogviewpager.StepDialog;
import com.zqg.dialogviewpager.ZoomOutPageTransformer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static android.content.ContentValues.TAG;
import static com.example.completemute.MainActivity.aSwitch;
import static com.example.completemute.MainActivity.hSwitch;
import static com.example.completemute.MainActivity.uSwitch;

public class TouchActivity extends AppCompatActivity {
    private Context mContext;
    private boolean isCopy = false;
    private SwitchCompat sSwitch,pSwitch,oSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        mContext = this;

        Button btnTouchEvent = findViewById(R.id.btn_touchevent);
        btnTouchEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepDialog.getInstance()
                        .setImages(new int[]{R.drawable.new_user_guide_1, R.drawable.new_user_guide_2, R.drawable.new_user_guide_3, R.drawable.new_user_guide_4})
                        .setCanceledOnTouchOutside(true)
                        .setPageTransformer(new ZoomOutPageTransformer())
                        .setOnCancelListener(new StepDialog.OnCancelListener() {
                            @Override
                            public void onCancel(int position) {
                                Log.e("MainActivity", position + "");
                                //滑到最后一页会自动dissmiss,可以在这里做自己的操作
                            }
                        })
                        .show(getFragmentManager());
            }
        });

        //开源相关
        oSwitch=(SwitchCompat)findViewById(R.id.open_source);
        oSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(
                            TouchActivity.this);
                    mDialog.setTitle("开源相关");
                    mDialog.setMessage("https://github.com/nisrulz/sensey\n" +
                            "https://github.com/guangzq/StepDialog\n" +
                            "https://github.com/codemybrainsout/ahoy-onboarding\n" +
                            "https://github.com/shineqstm/CheckNetworkStatusChangeDemo\n" +
                            "https://github.com/rubensousa/ViewPagerCards\n"+
                            "https://github.com/chenjunyu19/MediaVolume");
                    mDialog.show();
                    oSwitch.setChecked(false);
                }
            }
        });

        //使用说明
        hSwitch=(SwitchCompat)findViewById(R.id.switch_help);
        hSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(
                            TouchActivity.this);
                    mDialog.setTitle("使用说明");
                    mDialog.setMessage("正常模式:关闭所有声音，" +
                            "适用于没有或未启用勿扰模式的系统，但"+
                            "也有些系统需要开启并选择正确的勿扰规则"+
                            "才能使用。\n" +
                            "兼容模式:只关闭媒体、通话和闹钟，因为自带" +
                            "勿扰模式的系统打开勿扰会自己关闭铃" +
                            "声和系统声音，不允许更改。\n" +
                            "贴耳息屏:通过距离传感器来实现贴近手" +
                            "机就息屏，离开手机就亮屏。\n" +
                            "时间间隔:自定义检测控制静音的时间，" +
                            "默认值100毫秒，当用户不小心打开声" +
                            "音时，会根据设置检测的时间快速调成" +
                            "静音。\n" +
                            "听筒切换:自定义切换媒体为扬声器播放" +
                            "或者听筒播放，该功能不仅可以用来对付" +
                            "流氓软件，还可以解决没带耳机刷视频，"+
                            "听歌影响他人的烦恼。\n" +
                            "测试音源:用来测试音频播放途径，避免听"+
                            "筒切换失败。测试时为防止影响他人，请将"+
                            "音量调至最低,确定无误方可逐渐扩大。\n" +
                            "音量控制:一个简单的音量控制面板。\n" +
                            "桌面小部件:专门为低版本不能使用磁贴" +
                            "功能的手机设计，该功能可以提供更便捷" +
                            "的操作。\n" +
                            "备注:正常模式由于系统的不同使用方法不" +
                            "唯一，一般原生手机只要勿扰选择规则不是" +
                            "仅限优先打扰，都可以正常启用，测试结果" +
                            "显示区别在于：\n" +
                            "开启勿扰情况下：\n"+
                            "1、仅限优先打扰：可以关闭所有声音，但" +
                            "只能恢复闹钟和媒体。\n"+
                            "2、仅限闹钟：可以关闭所有声音，但" +
                            "只能恢复闹钟、通话和媒体。\n"+
                            "3、完全静音：可以关闭所有声音，但" +
                            "只能恢复通话。\n"+
                            "未开启勿扰情况下：\n"+
                            "只关闭闹钟和媒体，可恢复。\n"+
                            "兼容模式勿扰下除仅限优先打扰和未开启勿扰可" +
                            "以多恢复通话，其余基本一致。\n"+
                            "具体情况根据用户的系统而定,该功能最终目的" +
                            "只是为了防止有声音漏关影响他人及保护隐私。");
                    mDialog.setPositiveButton("我明白了",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(TouchActivity.this, "嘿嘿~你真棒~", Toast.LENGTH_SHORT).show();
                                }
                            });
                    mDialog.setNegativeButton("我用过了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(TouchActivity.this, "哎呦~不错哦~", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mDialog.show();
                    hSwitch.setChecked(false);
                }
            }
        });

        //打开插件
        uSwitch=(SwitchCompat) findViewById(R.id.switch_tools);
        uSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    try {
                        Intent intent = new Intent();
                        ComponentName cn = new ComponentName("com.example.tools", "com.example.tools.MainActivity");
                        intent.setComponent(cn);
                        Uri uri = Uri.parse("tools");
                        intent.setData(uri);
                        startActivity(intent);
                        uSwitch.setChecked(false);
                    }catch (Exception e)
                    {
                        Toast.makeText(mContext, "请先安装插件", Toast.LENGTH_SHORT).show();
                        uSwitch.setChecked(false);
                    }
                }
            }
        });

        //安装插件
        aSwitch=(SwitchCompat)findViewById(R.id.switch_install);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    aSwitch.setChecked(false);
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(
                            TouchActivity.this);
                    mDialog.setTitle("温馨提示");
                    mDialog.setMessage("默认本地解压安装，如果您跳转商店或者网页，请开启软件“存储空间”权限。");
                    mDialog.setPositiveButton("我已开启",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    install();
                                }
                            });
                    mDialog.setNegativeButton("下载安装",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = Uri.parse("https://www.lanzous.com/b00nbmrba");
                                    Intent intent   = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(intent);
                                }
                            });
                    mDialog.show();
                }
            }
        });

        //隐私策略
        pSwitch=(SwitchCompat)findViewById(R.id.privacy_policy);
        pSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    pSwitch.setChecked(false);
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(
                            TouchActivity.this);
                    mDialog.setTitle("隐私策略");
                    mDialog.setMessage("我们不会收集及以任何形式儲存來自你社交网路的任何资讯或銷售" +
                            "給广告或其它运营机构。使用本APP即表示您同意此隐私政策的条款和条件。如果" +
                            "您不同意本政策，請不要使用該APP。我們保留权利，在我們決定更改，修改，增" +
                            "加或刪除本政策的部分，在任何時候。請定期浏览此网页查阅任何修改。如果您继" +
                            "续使用我們的App以後的任何更改這些条款的发布将意味著你已经接受了這些调整。\n\n软件隐私权限说明：\n" +
                            "\n" +
                            "1. 允许应用更改您的音频设置；\n" +
                            "说明：此权限用于用户使用本产品静音以及使用听筒播放。\n" +
                            "2. 允许应用修改或删除您的USB存储设备中的内容；\n" +
                            "说明：修改或删除您的USB存储设备中的内容主要用于应用的更新以及删除安装包。\n" +
                            "3. 允许应用安装应用；\n" +
                            "说明：用于更新时安装应用，软件功能需要；\n" +
                            "4. 允许完全的网络访问权限。\n" +
                            "说明：用于获取更新及公告；\n" +
                            "5. 允许应用查看网络连接；\n" +
                            "说明：用于检查网络连接状态。\n" +
                            "6. 允许应用防止手机休眠；\n" +
                            "说明：用于保持应用处于活跃状态不被清理。");
                    mDialog.setPositiveButton("同意",null);
                    mDialog.setNegativeButton("暂不使用",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            });
                    mDialog.show();
                }
            }
        });

        //服务协议
        sSwitch=(SwitchCompat)findViewById(R.id.service_agreement);
        sSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    sSwitch.setChecked(false);
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(
                            TouchActivity.this);
                    mDialog.setTitle("服务协议");
                    mDialog.setMessage("用户在使用技术开发方（即，以下统称“技术开发方”）提供的各项服务之前，应仔细阅读本《用户协议》（以下简称“本协议”）。用户一旦使用技术开发方的服务，即视为用户已了解并明示同意本协议各项内容，本协议立即在用户与本技术开发方之间产生法律效力。用户使用本产品服务的全部活动将受到本协议的约束并承担相应的责任和义务。如用户不同意本协议任何内容的，请用户立即停止使用技术开发方所提供的全部服务。 根据《中华人民共和国网络安全法》、《电信和互联网用户个人信息保护规定》及相关法律法规的规定，同时依据技术开发方与其合作伙伴之间的相关协议，用户必须已明示授权技术开发方合作伙伴（以下简称“合作伙伴”）、在此明示授权并委托技术开发方及其关联公司通过官方或相关实名认证平台、信用信息平台（包括但不限于：征信机构、银行信用信息平台、网络借贷平台、消费金融平台、第三方支付平台、公积金平台、投资理财平台等）及其它相关平台查询、验证、存储用户的个人信用信息，并输出给合作伙伴对用户的个人信用进行评估与参考使用。用户理解并同意，具体的授权查询、验证、存储及输出的内容以合作伙伴要求查询、验证、存储、输出以及技术开发方及其关联公司实际查询、验证、存储、输出的信息为准。技术开发方及其关联公司会在授权范围内对相关个人信息予以处理（包括但不限于为保护用户个人信息而加密处理、掩码处理等一切为实现相关协议目的而进行的所有必要处理）并仅提供给合作伙伴使用，但用户与合作伙伴之间因授权的有效性、授权内容、授权范围、授权期限等发生的争议纠纷与技术开发方无关。如用户对合作伙伴的上述授权事项有任何异议或争议的，应立即停止使用技术开发方所提供的全部服务。用户使用本服务的，即表明用户已明示对合作伙伴、技术开发方及其关联公司授权查询、验证、处理、存储、在约定范围内使用其个人信息，并对授权的效力、查询验证的内容、查询验证平台、处理方式、使用范围等相关事项无任何事实或法律上的异议或争议。 鉴于用户须授权合作伙伴并由该合作伙伴告知本服务后方能进入、登录并使用本服务，用户登录或使用本服务时起即视为用户与技术开发方的合作伙伴之间已存在合法的、充分的、必要的、不可撤销的授权，且用户已清楚知晓其授权提供相关信息可能对其造成的相关不利影响，如负面的信用评价等。为保护用户个人信息，技术开发方会采取合理措施对用户信息进行严格保密，同时督促并要求该特定合作伙伴进行严格保密。未经用户授权，技术开发方及其关联公司不会将用户信息提供给任何其他方。\n" +
                            "\n" +
                            "一、协议主体\n" +
                            "本协议是用户与技术开发方关于用户使用本服务所订立的协议。\n" +
                            "\n" +
                            "二、关于本服务\n" +
                            "本服务内容是指技术开发方通过本应用程序向其合作伙伴及用户提供的相关服务（简称“本服务”）。\n" +
                            "对用户使用的本服务，技术开发方会不断丰富用户使用本服务的终端、形式等。\n" +
                            "许可的范围：\n" +
                            "-（1）技术开发方授予用户一项个人的、不可转让及非排他性的许可，以使用本应用程序。\n" +
                            "-（2）本条及本协议其他条款未明示授权的其他一切知识产权权利仍由技术开发方保留。技术开发方如果未行使前述任何权利，并不构成对该权利的放弃。\n" +
                            "\n" +
                            "三、应用程序的使用\n" +
                            "如果用户从非技术开发方合作伙伴的应用程序或非技术开发方合作伙伴处获取本应用程序或与本应用程序名称相同的安装程序，技术开发方无法保证该应用程序能够正常使用，并对因此给用户造成的损失不予负责。\n" +
                            "\n" +
                            "四、应用程序的更新\n" +
                            "为了增进用户体验、完善服务内容，技术开发方将不断努力为用户时时提供应用程序更新（这些更新可能会采取应用程序替换、修改、功能强化、版本升级等形式）。\n" +
                            "为了改善用户体验，并保证服务的安全性和功能的一致性，技术开发方有权不经向用户特别通知而对应用程序进行更新，或者对应用程序的部分功能效果进行改变或限制。\n" +
                            "\n" +
                            "五、授权事项及行为规范\n" +
                            "授权事项\n" +
                            "-（1）用户充分理解并同意：用户在使用本服务时，可能需要使用用户终端设备的相关权限、接口及相关设备信息等才能实现相应的功能。\n" +
                            "-（2）用户可以选择不向技术开发方提供用户的某些信息，但因此可能会导致相关服务功能无法实现。\n" +
                            "-（3）为实现本协议目的为合作伙伴及用户提供更加优质、安全的服务，用户同意并明示授权技术开发方及其关联公司对用户的相关个人信息进行查询、验证、存储、处理并在约定范围内使用（提供给用户已授权的合作伙伴对用户进行信用评估及参考使用）。技术开发方及其关联公司对用户的个人信息进行严格保密。本协议项下的授权为不可撤销的授权。\n" +
                            "-（4）用户知晓并明示授权同意技术开发方及其关联公司依据相关法律法规的规定，受合作伙伴委托向第三方征信机构或数据机构等合法查询、验证、审核用户信息，上述信息包括但不限于个人基本信息、特征信息（包括但不限于用户的法院失信信息、网络失信信息、是否为羊毛党信息、是否曾使用通讯小号及可疑设备信息等）、关联信息（即用户的身份证信息、手机号、手机设备及银行卡之间的关联关系，以判断用户信息是否有异常，该关联关系不涉及具体的个人敏感信息）、借贷交易信息、网络投资理财信息、公积金信息、公用事业信息、央行征信报告、个人网络数据信息等合作伙伴需要验证或参考使用的相关用户信息。技术开发方具体查询、验证及审核的信息最终以合作伙伴需要验证、需要参考使用及实际验证与使用的信息为准。技术开发方对所获取的信息，仅在用户与合作伙伴之间有关个人信用信息评估等合作伙伴业务相关工作中使用。技术开发方及其关联公司将对所获取的信息向该合作伙伴进行提供，除此之外，未经用户授权，技术开发方及其关联公司不得也不会向其他机构或个人泄露、披露或提供用户的信息\n" +
                            "用户禁止行为除非法律允许或技术开发方书面许可，用户不得从事下列行为：\n" +
                            "-（1）删除本应用程序及其副本上关于著作权的信息。\n" +
                            "-（2）对本应用程序进行反向工程、反向汇编、反向编译，或者以其他方式尝试发现本应用程序的源代码。\n" +
                            "-（3）对技术开发方拥有知识产权的内容进行使用、出租、出借、复制、修改、链接、转载、汇编、发表、出版等。\n" +
                            "-（4）通过修改或伪造应用程序运行中的指令、数据，增加、删减、变动应用程序的功能或运行效果，或者将用于上述用途的应用程序、方法进行运营或向公众传播，无论这些行为是否为商业目的。\n" +
                            "-（5）自行、授权他人或利用第三方应用程序对本应用程序及其组件、模块、数据等进行干扰。\n" +
                            "-（6）其他未经技术开发方明示授权的行为。\n" +
                            "对自己行为负责用户充分了解并同意，用户必须为自己对合作伙伴的授权（包括但不限于授权方式、授权内容及授权期限等）以及其账户下的相关行为负责。技术开发方会督促合作伙伴获取用户的授权后方能对相关信息进行查询、验证或使用并要求合作伙伴对用户的信息进行严格保密，但用户应对使用本服务时接触到的内容自行加以判断，如对授权相关事项及信息安全有任何异议或争议的，应立即停止使用本服务。技术开发方无法且不会对用户与合作伙伴之间的任何争议或纠纷而承担责任，用户未按约定要求立即停止使用本服务的，技术开发方对非因技术开发方的原因而产生的任何风险或损失将不承担任何责任。\n" +
                            "\n" +
                            "六、知识产权声明\n" +
                            "技术开发方是本应用程序的知识产权权利人。本应用程序的著作权、商标权、专利权、商业秘密等知识产权，以及与本应用程序相关的所有信息内容（包括但不限于文字、图片、音频、视频、图表、界面设计、版面框架、有关数据或电子文档等）均受中华人民共和国法律法规和相应的国际条约保护，技术开发方依法享有上述相关知识产权，但相关权利人依照法律规定应享有的权利除外。\n" +
                            "未经技术开发方或相关权利人书面同意，用户不得为任何商业或非商业目的自行或许可任何第三方实施、利用、转让上述知识产权。\n" +
                            "\n" +
                            "七、终端安全责任\n" +
                            "用户理解并同意，本应用程序或本服务同大多数互联网应用程序、服务一样，可能会受多种因素影响（包括但不限于用户原因、网络服务质量、社会环境等）；也可能会受各种安全问题的侵扰（包括但不限于他人非法利用用户资料，进行现实中的骚扰；用户下载安装的其他应用程序或访问的其他网站中可能含有病毒、木马程序或其他恶意程序，威胁用户终端的信息和数据的安全，继而影响本应用程序、本服务的正常使用等）。因此，用户应加强信息安全及个人信息的保护意识，注意密码保护，以免遭受损失。出现上述情况时技术开发方将努力在第一时间与相关方配合，及时进行修复，但是由此给用户造成的损失技术开发方在法律允许的范围内免责。\n" +
                            "用户不得制作、发布、使用、传播用于窃取技术开发方账号及他人个人信息、财产的恶意程序。\n" +
                            "维护应用程序安全与正常使用是技术开发方和用户的共同责任，技术开发方将按照行业标准合理审慎地采取必要技术措施保护用户的终端信息和数据安全。\n" +
                            "在法律允许的范围内，技术开发方对以下情形导致的服务中断或受阻不承担责任：\n" +
                            "-（1）受到计算机病毒、木马或其他恶意程序、黑客攻击的破坏。\n" +
                            "-（2）用户或技术开发方的电脑软件、系统、硬件和通信线路出现故障。\n" +
                            "-（3）用户操作不当。\n" +
                            "-（4）用户通过非技术开发方授权的方式使用本服务。\n" +
                            "-（5）其他技术开发方无法控制或合理预见的情形。\n" +
                            "\n" +
                            "八、不可抗力及合理免责\n" +
                            "“不可抗力”是指在本协议签订后发生的、受影响一方无法预见、无法避免并无法克服的客观情况。此等事件包括但不限于水灾、火灾、旱灾、台风、地震及其它自然灾害、罢工、骚动、暴乱及战争以及政府部门的作为或不作为、法律法规或政策调整、数据来源变更(包括但不限于其服务内容及形式的变更)、国内数据渠道瘫痪、黑客攻击、计算机病毒侵入、新型病毒爆发、因电信运营商问题导致网络中断服务器不可访问、停电、系统故障、传输线路、通信故障等技术开发方无法控制的因素。因受不可抗力影响而不能履行或不能完全履行本协议的不视为违约，不应承担相应违约责任 。\n" +
                            "\n" +
                            "九、其他\n" +
                            "用户使用本应用程序或本服务即视为用户已阅读并同意受本协议的约束。技术开发方有权在必要时修改本协议条款。用户可以在本应用程序、本服务的最新版本中查阅相关协议条款。本协议条款变更后，如果用户继续登录、使用本应用程序、本服务，即视为用户已接受修改后的协议。如果用户不接受修改后的协议，应当停止使用本应用程序。\n" +
                            "本协议的成立、生效、履行、解释及纠纷解决，适用中华人民共和国大陆地区法律（不包括冲突法）。\n" +
                            "若用户和技术开发方之间发生任何纠纷或争议，首先应友好协商解决；协商不成的，用户同意将纠纷或争议提交被告方所在地人民法院管辖。\n" +
                            "本协议所有条款的标题仅为阅读方便，本身并无实际涵义，不能作为本协议涵义解释的依据。\n本协议条款无论因何种原因部分无效或不可执行，其余条款仍有效，对双方具有约束力。");
                    mDialog.setPositiveButton("同意",null);
                    mDialog.setNegativeButton("暂不使用",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            });
                    mDialog.show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(
                TouchActivity.this);
        mDialog.setTitle("Confirm Back..!!!");
        mDialog.setMessage("Are you sure,You want to back?");
        mDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(TouchActivity.this, MainActivity.class));
                        finish();
                    }
                });
        mDialog.setNegativeButton("No", null);
        mDialog.show();
    }


    /**
     * 安装应用程序
     */
    public void install(){
        if(copyToSDCard(this,"tools.apk", Environment.getExternalStorageDirectory().getAbsolutePath()+ "/tools.apk")){
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext).setTitle("温馨提示").setMessage("该插件将帮助您及时获得最新版本").setPositiveButton("解压安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/tools.apk";
                    Log.i(TAG, "开始执行安装: " + filePath);
                    File apkFile = new File(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Log.w(TAG, "版本大于 N ，开始使用 fileProvider 进行安装");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(
                                mContext
                                , "com.example.completemute.fileprovider"
                                , apkFile);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        Log.w(TAG, "正常进行安装");
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    }
                    startActivity(intent);
                    Log.e("程序已安装","ok");
                }
            }).setNegativeButton("我已安装", null);
            builder.show();
        }
        else{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.example.tools"));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                intent.setData(Uri.parse("https://www.coolapk.com/apk/com.example.tools"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "天啊，您没安装应用市场，连浏览器也没有，我实在无能为力鸭~", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 将assests的资源复制到SD中
     */
    public boolean copyToSDCard(Context context, String fileName, String path){
        isCopy = false;
        try{
            InputStream inputStream = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while((i = inputStream.read(temp))>0){
                fileOutputStream.write(temp,0,i);
            }
            fileOutputStream.close();
            inputStream.close();
            isCopy = true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return isCopy;
    }
}
