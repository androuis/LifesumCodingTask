LifesumCodingTask
=================

The app can search for food, store it locally and view some details about it.

To search for food, use the search from the action bar. After search returns some results you can save items locally, either by selecting them and then pressing the save icon or by selecting the "Save all" option from menu.

In the "Offline products" tab, only the saved items are displayed ordered alphabetically. These items can be deleted by selecting them and then pressing the delete button or by selecting "Delete all" from menu. Also, searching is possible through this items, too.

For the UI, I used: view pager with tabs - I think it's the most suitable way to display the information for this case.

To perform the request, I used a thread with a handler to post results to UI (found a bug: forgot to display a progress bar while the request is being made).

To perform CRUD operations, I used as Sugar ORM and in order to perform save/search/delete, I used some async tasks (because these operations should be reflected on the UI).

List fragments are used to display the pages of the view pager, because fragments are more powerful than views and receive lifecycle callbacks.

Things to improve:
- instead of displaying loading dialogs, switch to progress bar, in order to let the user interact with the app
- change the layout for the list item - add more info & make it nicer
- add a star/checkmark to suggest the saved state of the item + use this element to save the item locally
- I think I have some memory/threading issues - must do some investigations on this part
- when selecting an item, display the information of the item as food product labels
- display items using some criteria, eg: category, amount of proteins, etc.
