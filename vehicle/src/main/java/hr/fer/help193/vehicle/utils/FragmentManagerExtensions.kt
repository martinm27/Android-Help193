package hr.fer.help193.vehicle.utils

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) = beginTransaction().func().commit()

fun FragmentManager.inTransactionAndAddToBackStack(name: String? = null, func: FragmentTransaction.() -> FragmentTransaction) = beginTransaction().func().addToBackStack(name).commit()
