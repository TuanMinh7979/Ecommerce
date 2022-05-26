callCategoriesData()

function callCategoriesData() {
    $.ajax({
        type: "get",
        url: "/ajax/hierarchical-category",
        success: function (data) {

            renderCatHierarchical(data);
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not load this categories in hierarchical',
                // text: 'Something went wrong!',

            })
        }
    });
}

$(function () {

    $("#shorDescription").richText();
    $("#fullDescription").richText();


})

function renderCatHierarchical(data) {
    let opts = "";

    data.map(function (cat) {
        opts += `<option value="${cat.id}" >${cat.name}</option> `

    })


    $("#category-sel").append(opts);

}
