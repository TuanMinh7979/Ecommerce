var mode = "";
var numOfImage = 1;
var MAX_FILE_SIZE = 512000;

$(function () {

    $("#roles-sel").select2();
    if (document.getElementById("userId-inp") != null) {
        mode = "edit";
        loadActiveRoleIds();
    }
    $("#selectnone-btn").on("click",
        function (event) {
            event.preventDefault();
            handleSelectDefaultBtn(this, mode, "delImageId", defaultImage);
        })
})


$(document).on("change", ".file_inp", function (event) {
    event.preventDefault();
    changeImage(this.files[0], this, mode, "delImageId", defaultImage)

});


const loadActiveRoleIds = () => {
    let userId = $("#userId-inp").val();
    url = `/admin/user/api/${userId}/active-role-ids`;
    let roleSelectOptions = $('#roles-sel option');

    $.ajax({
        type: "get",
        url: url,
        contentType: "application/json",
        success: function (data) {
            let activeIdArr = [];
            data.map(function (idi) {

                    $.each(roleSelectOptions, function (index, value) {
                        if (value.value == idi) {
                            let opt = $(value);
                            activeIdArr.push(opt.attr('value'));
                            return;
                        }

                    })
                }
            )

            $("#roles-sel").val(activeIdArr).trigger('change');
        },
        error: function () {

        }


    })

}












