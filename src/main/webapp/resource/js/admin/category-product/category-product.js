function setActiveCheckbox() {
    $(".atb-active-checkbox").each(function () {

        let isActive = $(this).attr("isActive") == 1 ? true : false;

        if (isActive == 1) {
            $(this).css("color", "blue");
        } else {
            $(this).css("color", "red");
        }


    })


}


function pushNewRecord(pushAtrBtn) {
    let newtask = $(pushAtrBtn).parent();
    let newtaskInput = $(newtask).find("input");
    if (newtaskInput.val().length == 0) {

        alert("Please Enter a Task");
    } else {
        let tasksWrapper = newtask.parent().find(".tasks");
        let rs = "";
        rs += `
            <div class="task">
                <span class="atrValueSpan" class="taskname">
                   ${newtaskInput.val()}
                </span>
                <button class="delete">
                    <i class="far fa-trash-alt"></i>
                </button>
            </div>
        `;
        tasksWrapper.append(rs);

    }
}

function existByName(keyName, atbs) {
    // for (let atbi in atbs) {
    //     if (atbs[atbi].name === name) {
    //         return true;
    //     }
    // }
    // return false;

    if (atbs.hasOwnProperty(keyName)) {
        return true;
    }

    return false;
}


function updateAttribute(keyName, newAttribute, atbs) {
    if (atbs.hasOwnProperty(keyName)) {
        atbs[keyName] = newAttribute;
        return;
    }

    return;
}



