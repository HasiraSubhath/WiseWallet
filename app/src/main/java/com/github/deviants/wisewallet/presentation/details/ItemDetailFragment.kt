package com.github.deviants.wisewallet.presentation.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.deviants.wisewallet.R
import com.github.deviants.wisewallet.data.transaction.Transaction
import com.github.deviants.wisewallet.data.transaction.Transaction.Companion.INCOME
import com.github.deviants.wisewallet.data.transaction.getFormattedPrice
import com.github.deviants.wisewallet.databinding.FragmentItemDetailBinding
import com.github.deviants.wisewallet.presentation.ExpensesApplication
import com.github.deviants.wisewallet.presentation.list.ExpensesViewModelFactory
import com.github.deviants.wisewallet.presentation.list.TransactionsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class ItemDetailFragment : Fragment() {
    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionsViewModel by activityViewModels {
        ExpensesViewModelFactory((activity?.application as ExpensesApplication).database.itemDao())
    }

    lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) {
            transaction = it
            bind(transaction)
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question)).setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }.show()
    }

    private fun deleteItem() {
        viewModel.deleteItem(transaction)
        findNavController().navigateUp()
    }


    private fun bind(transaction: Transaction) {
        binding.apply {
            itemCategory.text = transaction.transactionCategory
            itemPrice.text = transaction.getFormattedPrice()
            itemIsCompulsory.text = when (transaction.transactionType) {
                INCOME -> getString(R.string.income)
                else -> when (transaction.isCompulsory) {
                    true -> {
                        getString(R.string.expense) + " (${getString(R.string.compulsory).lowercase()})"
                    }
                    false -> {
                        getString(R.string.expense) + " (${getString(R.string.notCompulsory).lowercase()})"
                    }
                }
            }

            val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm ")
            itemDate.text = simpleDateFormat.format(transaction.date)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            itemDescription.text = transaction.transactionDescription

            //editItemButton.setOnClickListener { editItem() }

            //deleteItem.setOnClickListener { showConfirmationDialog() }
            editItemFab.setOnClickListener { editItem() }
        }

    }

    private fun editItem() {
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
                transaction.id,
                getString(R.string.edit_fragment_title))
        this.findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                showConfirmationDialog()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
