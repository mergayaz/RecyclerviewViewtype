package kz.kuz.recyclerviewviewtype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

// В этом упражнении notifyItemMoved не работает корректно по причине разных view,
// необходимо разбираться
class MainFragment : Fragment() {
    private lateinit var mMainRecyclerView: RecyclerView
    private lateinit var mAdapter: MainAdapter

    //    int positionToUpdate;
    private inner class Item {
        var mTitle: String? = null
        var mPart1: String? = null
        var mPart2: String? = null
        var viewType = 0
    }

    private val items: MutableList<Item> = ArrayList()

    // методы фрагмента должны быть открытыми
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.toolbar_title)
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mMainRecyclerView = view.findViewById(R.id.main_recycler_view)
        mMainRecyclerView.layoutManager = LinearLayoutManager(activity)
        for (i in 0..19) {
            val item: Item = Item()
            item.mTitle = "Title #" + (i + 1)
            item.mPart1 = "Part1 #" + (i + 1)
            item.mPart2 = "Part2 #" + (i + 1)
            item.viewType = if (i % 2 == 0) 1 else 0
            items.add(item)
        }
        mAdapter = MainAdapter(items)
        mMainRecyclerView.adapter = mAdapter
        return view
    }

    private inner class MainAdapter(private val mItems: List<Item>) :
            RecyclerView.Adapter<MainAdapter.MainHolder>() {

        private inner class MainHolder(view: View?) : RecyclerView.ViewHolder(view!!),
                View.OnClickListener {
            val mTitleTextView: TextView = itemView.findViewById(R.id.item_title)
            val mPart1TextView: TextView = itemView.findViewById(R.id.item_part1)
            val mPart2TextView: TextView = itemView.findViewById(R.id.item_part2)

            override fun onClick(view: View) {
//            Toast.makeText(activity, mItems[adapterPosition].mTitle + " clicked!", Toast.LENGTH_SHORT)
//                    .show();
                mMainRecyclerView.adapter?.notifyItemMoved(adapterPosition, 0)
                mMainRecyclerView.scrollToPosition(0)
            }

            init {
                itemView.setOnClickListener(this) // реализуется слушатель на нажатие
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view: View
            view = if (viewType == 1) {
                layoutInflater.inflate(R.layout.list_item2, parent, false)
            } else {
                layoutInflater.inflate(R.layout.list_item, parent, false)
            }
            return MainHolder(view)
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
//            positionToUpdate = position;
            val item = mItems[position]
            holder.mTitleTextView.text = item.mTitle
            holder.mPart1TextView.text = item.mPart1
            holder.mPart2TextView.text = item.mPart2
        }

        override fun getItemCount(): Int {
            return mItems.size
        }

        override fun getItemViewType(position: Int): Int {
            return mItems[position].viewType
        }
    }

    override fun onResume() {
        super.onResume()
        mAdapter.notifyDataSetChanged() // обновление в случае изменений в списке
        //        mAdapter.notifyItemChanged(positionToUpdate); // для обновления одной позиции
    }
}