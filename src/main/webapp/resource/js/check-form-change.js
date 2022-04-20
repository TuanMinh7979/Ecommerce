$("#mainForm").data("changed", false);

$("#mainForm").on("change", function () {
    $(this).data("changed", true);
});
$('#mainForm').on('submit', function () {
    if (!$(this).data("changed")) {
        alert("Nothing changed!");
        return false;
        // Reset variable
    }
    // Do whatever you want here
    // You don't have to prevent submission

});

var generateID = function () {
    // Math.random should be unique because of its seeding algorithm.
    // Convert it to base 36 (numbers + letters), and grab the first 9 characters
    // after the decimal.
    return Math.random().toString(36).substr(2, 3);
};