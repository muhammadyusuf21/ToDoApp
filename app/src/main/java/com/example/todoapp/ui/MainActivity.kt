package com.example.todoapp.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.todoapp.R
import com.example.todoapp.ui.adapter.NoteAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var noteAdapter: NoteAdapter

    // Method #1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.container)


        //Floating action button will redirect to Add New Notes Fragment #AddFragment
        fab.setOnClickListener {
            onFloatingClicked()
        }
    }


    // Method #2
    private fun onFloatingClicked() {
        navController.navigate(R.id.action_listFragment_to_addFragment)
        fab.hide()
    }

    // Method #3
    fun showFloatingButton(){
        fab.show()
        fab.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menu.findItem(R.id.menu_search)).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Search Note..."
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                noteAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                noteAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}
