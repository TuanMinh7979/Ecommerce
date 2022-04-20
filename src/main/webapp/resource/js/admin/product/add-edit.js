var mode = "";

//FOR IMAGES SCREEN
var numOfImage = 1;
var MAX_FILE_SIZE = 512000;

$(function () {

    $("#shorDescription").richText();
    $("#fullDescription").richText();
    if (document.getElementById("productId") != null) {
        mode = "edit";
    }

    $("#selectnone-btn").on("click",
        function (event) {
            event.preventDefault();
            handleSelectDefaultBtn(this, mode, "delImageIds", defaultImage);
        })
})


$(document).on("change", ".file_inp", function (event) {
    event.preventDefault();
    changeImage(this.files[0], this, mode, "delImageIds", defaultImage)

});


function createNewEmptyExtraImage() {


    event.preventDefault();
    let html = '       <div class="col-6">\n' +
        '\n' +
        '<span>Extra</span>' +
        '            <div class="image-preview">\n' +
        '                <i class="close-i fas fa-times"></i>\n' +
        `                <img  src="${defaultImage}" alt="alt" class="image-preview__img"/>\n` +
        '\n' +
        '\n' +
        '            </div>\n' +
        '            <input type="file" name="files" class="file_inp"/>\n' +
        '        </div>'


    $("#imageWrapper").append(html);


}


$(document).on("click", "#addNewImageExtraBtn", createNewEmptyExtraImage);


$(document).on("click", ".image-preview .close-i", function () {
    // alert(productId);
    if ($(this).parent().hasClass("saved-image-preview") && mode === "edit") {
        document.getElementById("delImageIds").value += this.parentElement.parentElement.id + " ";
    }

    $(this).parent().parent().hide();
})

//DETAIL ATTRIBUTE TAB
$(function () {

    $('.nav-tabs a').on('shown.bs.tab', function (event) {
        var editMode = $(event.target).text();

        if (editMode == "Detail") {

            callAttributeApi(`/admin/product/api/${productId}/attributes`);
            $("#addNewAttributeBtn").on("click", function (event) {
                event.preventDefault();
                $("#addModalBtn").click();
            })

            $(".saveAtrsBtn").on("click", function (event) {
                event.preventDefault();
                saveCurrentFormAttribute(this);

            });

            $("#tag_delete_many").on("click", function (event) {

                event.preventDefault();
                let checkboxes = $('tr input[type="checkbox"]:checked');
                for (checkbox of checkboxes) {
                    let nameToDel = $(checkbox).parent().parent().find("td:nth-child(2)").text();
                    removeAttributeByName(nameToDel, attributeArray);
                    $(checkbox).closest("tr").remove();

                }
            });
            $(document).on("click", "#saveallchangeBtn", function (event) {
                event.preventDefault();
                saveAllChange(attributeArray, $(this).attr("href"));

            })



            $(document).on("click", '.tag_delete_one', function (event) {
                event.preventDefault();
                let nameToDel = $(this).parent().parent().find("td:nth-child(2)").text();
                removeAttributeByName(nameToDel, attributeArray);
                $(this).closest("tr").remove();
            })


        }


    });

})

var generateID = function () {
    // Math.random should be unique because of its seeding algorithm.
    // Convert it to base 36 (numbers + letters), and grab the first 9 characters
    // after the decimal.
    return Math.random().toString(36).substr(2, 3);
};

function saveAllChange(data, url) {

    $.ajax({
        type: "post",
        url: url,
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function (res) {
            location.reload();
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


$(document).on("click", ".push", function (event) {

    event.preventDefault();
    pushNewRecord(this)


});
$(document).on("click", ".delete", function (event) {

    event.preventDefault();
    $(this).parent().remove();
});


$(document).on("click", ".editAttributeBtn", function (event) {
    event.preventDefault();
    //load data first
    loadAttributeToForm(this);
    //after that click modal btn
    $("#editModalBtn").click();

})

function renderDataForAttributeTable(data) {
    let rs = "";


    data.map(function (atbi) {


        rs += `       <tr class="col-12">
                <td class="col-1"><input type="checkbox" class="atb-iddel-checkbox" value="${atbi.id}"></td>
                <td class="col-5" class="atb-name-inp" ">${atbi.name}</td>
                <td class="col-3"><i class="atb-active-checkbox fas fa-circle" isactive="${atbi.active}"></i>  </td>
                <td class="col-3">
                    <button class="editAttributeBtn btn btn-default" >Edit</button>
                    <a class="btn btn-danger tag_delete_one"   >Delete</a>

                </td>

            </tr>`
    })
    // }

    $("#tabledata").html(rs);
    setActiveCheckbox();


}

function callAttributeApi(url) {

    $.ajax({
        type: "get",
        url: url,
        success: function (data) {

            attributeArray = data;

            renderDataForAttributeTable(data);
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

//-DETAIL ATTRIBUTE TAB

//COMMON METHOD
function loadAttributeToForm(editAttributeBtn) {
    let tri = $(editAttributeBtn).parent().parent();
    let triId = tri.find(".atb-iddel-checkbox").val();
    let triName = tri.find("td:nth-child(2)").text();
    let triActive = tri.find(".atb-active-checkbox");


    let editModal = $("#exampleModalCenter2");
    editModal.find("#atrIdInp").val(triId);
    editModal.find(".atrNameInp").val(triName);

    let isactive = triActive.attr("isactive") == 1 ? true : false;


    editModal.find(".atrActiveChbx").prop("checked", isactive);


    let valueList = editModal.find(".tasks");


    let currentAttribute = findAttributeById(triId, attributeArray);

    if (currentAttribute["value"] != undefined) {
        let rs = "";
        currentAttribute["value"].map(function (valuei) {
            rs +=
                `<div class="task">
                <span class="atrValueSpan" class="taskname">
                    ${valuei}
                </span>
                        <button class="delete">
                            <i class="far fa-trash-alt"></i>
                        </button>
                    </div>`;


        });
        valueList.html(rs);
    }
}


function saveCurrentFormAttribute(saveFormBtn){


    let data = {}
    let modalContent = $(saveFormBtn).parent().parent();
    let attributeWrapper = modalContent.find(".attributeWrapper");

    data["name"] = attributeWrapper.find(".atrNameInp").val();
    data["active"] = attributeWrapper.find(".atrActiveChbx").is(":checked") ? 1 : 0;
    let valueArr = [];
    attributeWrapper.find(".atrValueSpan").each(function () {
        valueArr.push($(this).text().replace(/\r?\n|\r/g, " ").trim());
    })
    data["value"] = valueArr;

    if (attributeWrapper.find("#atrIdInp").val() !== undefined) {
        //case update
        data["id"] = attributeWrapper.find("#atrIdInp").val();
        updateAttribute(data, attributeArray);
    } else {
        //case add new attribute -> check if existByName
        if (existByName(data["name"], attributeArray)) {
            alert(data["name"] + " is exist!");
        } else {
            //add new
            data["id"] = generateID();
            attributeArray.push(data);
        }
    }


    let curModal = saveFormBtn.closest(".modal");
    $(curModal).modal('hide');
    renderDataForAttributeTable(attributeArray);


}


//COMMON METHOD




















