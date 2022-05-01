$("#mainForm").data("changed", false);

$("#mainForm").on("change", function () {
    $(this).data("changed", true);
});
$('#mainForm').on('submit', function () {
    if (!$(this).data("changed")) {
        alert("Nothing changed!");
        return false;
      
    }


});

var generateID = function () {
    return Math.random().toString(36).substr(2, 3);
};