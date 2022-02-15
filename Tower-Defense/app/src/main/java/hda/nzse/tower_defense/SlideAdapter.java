package hda.nzse.tower_defense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import org.w3c.dom.Text;

public class SlideAdapter extends PagerAdapter {

    // Used to determine for which activity this class is used
    private Context context;
    // Used to dynamically instantiate layout XML file into its corresponding view objects
    private LayoutInflater layoutInflater;

    // Arrays containing data for slides:
    public int[] slide_images = {
            /**
             * Add your image ids here.
             * For example R.drawable.background
            */
            R.drawable.hilfe_onboarding,
            R.drawable.game_onboarding_turm_grass,
            R.drawable.game_onboarding_gegner_weg,
            R.drawable.game_onboarding_burg_leben,
    };

    public String[] slide_headings;

    public String[] slide_descriptions;

    public SlideAdapter(Context context){
        this.context = context;

        slide_headings = new String[]{
                context.getString(R.string.slide1_heading_text),
                context.getString(R.string.slide2_heading_text),
                context.getString(R.string.slide3_heading_text),
                context.getString(R.string.slide4_heading_text)
        };
        slide_descriptions = new String[]{
                context.getString(R.string.slide1_description_text),
                context.getString(R.string.slide2_description_text),
                context.getString(R.string.slide3_description_text),
                context.getString(R.string.slide4_description_text)

        };

    }

    /**
     * get Counts of all the slides used.
     *
     * @return Number of headings, which is equal to the number of slides.
     *
     */
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // ConstraintLayout is used in this project, otherwise use RelativeLayout
        return (view == (ConstraintLayout) object);
    }

    /**
     * Used for slide effect animation in onboarding activity.
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // inflate container with slide_layout
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        // Initialize views from slideLayout
        TextView slideLayout_header = (TextView) view.findViewById(R.id.slideLayout_Header);
        TextView slideLayout_desc = (TextView) view.findViewById(R.id.slideLayout_description);
        ImageView slideLayout_img = (ImageView) view.findViewById(R.id.slideLayout_image);

        // Set content of views
        // set image:
        slideLayout_img.setImageResource(slide_images[position]);
        // set header:
        slideLayout_header.setText(slide_headings[position]);
        // set description:
        slideLayout_desc.setText(slide_descriptions[position]);

        // Add and display view in container
        container.addView(view);

        return view;
    }

    /**
     * Stops layout from creating multiple slides after reaching the end of the slides
     *
     * @param container Container in which the Layout is placed
     * @param position Position for the arrays(image, header, description)
     * @param object The Layout which should be removed
     *
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((ConstraintLayout) object);
    }
}
