function deleteOnTable(event) {
    event.preventDefault();
    let url = $(this).attr("href");
    let that = $(this);
    Swal.fire({

        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "POST",
                url: url,
                success: function (data) {
                    that.closest("tr").remove();
                },
                error: function (data) {


                    // console.log(data.responseJSON);
                    Swal.fire({
                        icon: 'error',
                        title: 'Can not delete it',
                        // text: 'Something went wrong!',

                    })
                }


            })
        }
    })
}


function deleteManyOnTable(event) {
    event.preventDefault();
    let url = $(this).attr("href");
    // let that = $(this);


    var toDelChecboxs = [];
    var checkboxes = document.querySelectorAll('tr input[type="checkbox"]:checked');


    for (var i = 0; i < checkboxes.length; i++) {

        toDelChecboxs.push(parseInt(checkboxes[i].value))
    }
    console.log(toDelChecboxs);

    Swal.fire({

        title: 'Are you sure to delete these?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.value) {

            $.ajax({
                type: "post",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(toDelChecboxs),


                success: function (data) {
                    for (checkbox of checkboxes) {
                        checkbox.parentElement.parentElement.remove();

                    }


                },
                error: function (data) {

                    Swal.fire({
                        icon: 'error',
                        title: 'Can not delete',
                        // text: 'Something went wrong!',

                    })
                }


            })
        }
    })
}

function deleteManyOnTableByStringIdArray(event) {
    event.preventDefault();
    let url = $(this).attr("href");
    // let that = $(this);


    var toDelChecboxs = [];
    var checkboxes = document.querySelectorAll('tr input[type="checkbox"]:checked');


    for (var i = 0; i < checkboxes.length; i++) {

        toDelChecboxs.push(checkboxes[i].value);
    }
    console.log(toDelChecboxs);

    Swal.fire({

        title: 'Are you sure to delete these?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.value) {

            $.ajax({
                type: "post",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(toDelChecboxs),


                success: function (data) {
                    for (checkbox of checkboxes) {
                        checkbox.parentElement.parentElement.remove();

                    }


                },
                error: function (data) {

                    Swal.fire({
                        icon: 'error',
                        title: 'Can not delete',
                        // text: 'Something went wrong!',

                    })
                }


            })
        }
    })
}


function checkFileSize(file, fileInp, maxsize) {
    let maxsizeInKb = maxsize / 1024;
    if (file != null && file.size > maxsize) {

        fileInp.setCustomValidity("Image must less than " + maxsizeInKb + " Kb");
        fileInp.reportValidity();


        return false;
    } else {
        fileInp.setCustomValidity("");
        fileInp.reportValidity();
        return true;
    }
}

//SELECT DEFAULT IMAGE
function handleSelectDefaultBtn(btn, mode, delIdsInpId, defaultImage) {


    let imageInputWrapper = $(btn).parent();

    let imagePreview = imageInputWrapper.find(".image-preview");
    let imagePreviewImg = imagePreview.find(".image-preview__img");
    let idTodel = imageInputWrapper.attr("id") != null ? imageInputWrapper.attr("id") : null;


    if (idTodel != null && imagePreviewImg.attr("src") != defaultImage && mode === "edit") {
        addIdToDel(idTodel, delIdsInpId);
    }


    imageInputWrapper.find(".file_inp").val(null);
    imagePreviewImg.attr("src", defaultImage)
}


///SELECT DEFAULT IMAGE

function addIdToDel(idToAdd, delIdsInpId) {
    let delIdsInp = $("#".concat(delIdsInpId));
    let oldVal = delIdsInp.val();
    if (delIdsInpId.charAt(delIdsInpId.length - 1) === 's') {
        oldVal = oldVal.trim();
        delIdsInp.val(oldVal.concat(" " + idToAdd));
    } else {
        delIdsInp.val(idToAdd);
    }
    alert(delIdsInp.val())

}

function changeImage(file, fileInp, mode, delIdsInpId, defaultImage) {


    let imageInputWrapper = $(fileInp).parent();
    let imagePreview = imageInputWrapper.find(".image-preview")
    let imagePreviewImg = imagePreview.find(".image-preview__img");
    if (!checkFileSize(file, fileInp, MAX_FILE_SIZE)) {
        return;
    }

    if (file) {
        const reader = new FileReader();


        let readerJo = $(reader);
        readerJo.on("load", function () {

            imagePreviewImg.attr("src", this.result);
        })


        reader.readAsDataURL(file);


        if (imageInputWrapper.attr('id') != undefined && imagePreviewImg.attr("src") != defaultImage && mode === "edit") {
            addIdToDel(imageInputWrapper.attr('id'), delIdsInpId);
        }

    } else {

        imagePreviewImg.attr("src", defaultImage)

    }

}

//load content through ajax
function loadContentWithGet(url, renderMethod) {

    $.ajax({
        type: "get",
        url: url,
        success: function (data) {
            renderMethod(data);
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not call this Api',
                // text: 'Something went wrong!',

            })
        }


    });
}

//load content through ajax













