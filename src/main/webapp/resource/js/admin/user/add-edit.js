var mode = "";
var numOfImage = 1;
var MAX_FILE_SIZE = 512000;

$(function () {

    $("#roles-sel").select2();
    if (document.getElementById("userId-inp") != null) {
        mode = "edit";
        loadActiveRoleIds();
    }

})

$("#selectnone-btn").on("click",
    function (event) {
        event.preventDefault();
        let imageInputWrapper = $(this).parent();
        if (imageInputWrapper.prop("id") != "") {
            $("#delImageId").val(imageInputWrapper.prop("id"));
        }

        let imagePreview = imageInputWrapper.find(".image-preview");
        let imagePreviewImg = imagePreview.find(".image-preview__img");

        $(imageInputWrapper).find(".file_inp").val(
            ""
        );
        $(imagePreviewImg).attr("src", defaultImage)
    })


$(document).on("change", ".file_inp", function (event) {

    event.preventDefault();
    let imageInputWrapper = $(this).parent();
    if (imageInputWrapper.prop("id") != "") {
        $("#delImageId").val(imageInputWrapper.prop("id"));
    }

    let imagePreview = imageInputWrapper.find(".image-preview")
    let imagePreviewImg = imagePreview.find(".image-preview__img");
    if (!checkFileSize($(this).prop('files')[0], this, MAX_FILE_SIZE)) {
        return;
    }

    if ($(this).prop('files')[0]) {
        const reader = new FileReader();
        let readerJo = $(reader);
        readerJo.on("load", function () {
            imagePreviewImg.attr("src", this.result);
        })
        reader.readAsDataURL($(this).prop('files')[0]);
    } else {

        imagePreviewImg.attr("src", defaultImage)

    }
})
;


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

window.addEventListener("paste", (e) => {
    if (e.clipboardData.files.length > 0) {
        let fileinputs = []

        fileinputs = document.querySelectorAll(".file_inp");
        fileinputs.forEach(function (inp) {

            if ($(inp).parent().find('img').attr("src") === "/resource/img/defaultAvatar.jpg") {
                inp.files = e.clipboardData.files;
                let fileReader = new FileReader();

                fileReader.readAsDataURL(e.clipboardData.files[0]);
                fileReader.onload = () => {
                    $(inp).parent().find('img').attr("src", fileReader.result);

                }
                return;
            }


        })


    }
});












