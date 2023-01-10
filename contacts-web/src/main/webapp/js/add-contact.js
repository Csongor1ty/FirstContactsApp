function newRow(button){
   button = $(button);
   let newRowToAdd = button.closest('.row').clone(true);
   newRowToAdd.find('input').val('');

   button.html('Delete');
   button.attr('onclick', 'deleteRow(this)');
   let phoneRows = $('.row');
   phoneRows.last().parent().append(newRowToAdd);
}

function deleteRow(button){
   button.closest('.row').remove();
}
