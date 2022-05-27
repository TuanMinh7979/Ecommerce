var mode = "";
$(function () {

    if (document.getElementById("roleId-inp") != null) {
        mode = "edit";
        loadActivePermissionIds();
    }


})

const loadActivePermissionIds = () => {
    let roleId = $("#roleId-inp").val();
    url = `/admin/role/api/${roleId}/active-permission-ids`;
    let checkboxes = $('.card-body input[type=checkbox]');

    $.ajax({
        type: "get",
        url: url,
        contentType: "application/json",


        success: function (data) {
            data.map(function (idi) {

                    $.each(checkboxes, function (index, value) {
                        if (value.value == idi) {
                            jelm = $(value);
                            jelm.prop("checked", true)
                            return;
                        }

                    })
                }
            )
        },
        error: function () {

        }


    })

}




