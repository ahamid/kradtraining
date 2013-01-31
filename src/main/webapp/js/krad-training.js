function reorderIds() {
    alert("Ids have been reordered!");
}

function clearForm() {
    jQuery("#kualiForm").clearForm();
}

function promptBeforeSave() {
    var bookTitle =  coerceValue("book.title");

    var response = confirm("Are you sure you want to save the book: " + bookTitle + "?");

    return response;
}

function setupReviewCheck() {
    jQuery("input[name='book.averageReview']").click(window.checkReview);
}

function checkReview() {
    var self = jQuery(this), content = "";
    if (self.val() == "5") {
        content = "<b>Awesome!</b>";
    }
    jQuery('#averageReview_markers').html(content);
}

