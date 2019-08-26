package musubidevs.android.greenwich

import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.list_item_conversion.view.*
import musubidevs.android.greenwich.fragment.DatePickerFragment
import musubidevs.android.greenwich.fragment.TimePickerFragment
import musubidevs.android.greenwich.fragment.TimeZonePickerFragment
import musubidevs.android.greenwich.model.Conversion
import musubidevs.android.greenwich.model.SourceTimestamp
import musubidevs.android.greenwich.model.TargetTimestamp
import musubidevs.android.greenwich.model.Timestamp

/**
 * @author jmmxp
 * @author anticobalt
 */
class ConversionItem(
    val conversion: Conversion,
    private val fragmentManager: FragmentManager,
    private val adapter: FastAdapter<ConversionItem>
): AbstractItem<ConversionItem.ConversionViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.list_item_conversion

    override val type: Int
        get() = R.id.conversionView

    override fun getViewHolder(v: View): ConversionViewHolder {
        return ConversionViewHolder(v, fragmentManager, adapter)
    }

    class ConversionViewHolder(
        itemView: View,
        private val fragmentManager: FragmentManager,
        private val adapter: FastAdapter<ConversionItem>
    ) :
        FastAdapter.ViewHolder<ConversionItem>(itemView) {

        override fun bindView(item: ConversionItem, payloads: MutableList<Any>) {
            this.conversion = item.conversion
            this.sourceTimestamp = conversion.sourceTimestamp
            this.targetTimestamp = conversion.targetTimestamp

            sourceDateView.text = sourceTimestamp.dateString
            sourceTimeView.text = sourceTimestamp.timeString
            sourceTimeZoneView.text =
                itemView.context.getString(R.string.utc, sourceTimestamp.utcOffsetString)

            targetDateView.text = targetTimestamp.dateString
            targetTimeView.text = targetTimestamp.timeString
            targetTimeZoneView.text =
                itemView.context.getString(R.string.utc, targetTimestamp.utcOffsetString)
        }

        override fun unbindView(item: ConversionItem) {
            sourceDateView.text = null
            sourceTimeView.text = null
            sourceTimeZoneView.text = null
            targetDateView.text = null
            targetTimeView.text = null
            targetTimeZoneView.text = null
        }

        private lateinit var conversion: Conversion
        private lateinit var sourceTimestamp: SourceTimestamp
        private lateinit var targetTimestamp: TargetTimestamp
        private val sourceDateView: TextView = itemView.sourceDateView as TextView
        private val sourceTimeView: TextView = itemView.sourceTimeView as TextView
        private val sourceTimeZoneView: TextView = itemView.sourceTimeZoneView as TextView
        private val targetDateView: TextView = itemView.targetDateView as TextView
        private val targetTimeView: TextView = itemView.targetTimeView as TextView
        private val targetTimeZoneView: TextView = itemView.targetTimeZoneView as TextView

        init {
            setSourceOnClicks()
            setTargetOnClicks()
        }

        private fun setSourceOnClicks() {
            sourceDateView.setOnClickListener {
                DatePickerFragment(sourceTimestamp) { update(it) }.show(
                    fragmentManager,
                    "datePicker"
                )
            }
            sourceTimeView.setOnClickListener {
                TimePickerFragment(sourceTimestamp) { update(it) }.show(
                    fragmentManager,
                    "timePicker"
                )
            }
            sourceTimeZoneView.setOnClickListener {
                TimeZonePickerFragment(sourceTimestamp) { update(it) }.show(
                    fragmentManager,
                    "timeZonePicker"
                )
            }
        }

        private fun setTargetOnClicks() {
            targetTimeZoneView.setOnClickListener {
                TimeZonePickerFragment(targetTimestamp) { update(it) }.show(
                    fragmentManager,
                    "timeZonePicker"
                )
            }
        }

        private fun update(newTimestamp: Timestamp) {
            conversion.updateTimestamp(newTimestamp)
            adapter.notifyItemChanged(adapterPosition)
        }

        fun getEditableTextViews(): List<TextView> {
            return listOf(sourceDateView, sourceTimeView, sourceTimeZoneView, targetTimeZoneView)
        }
    }

}

