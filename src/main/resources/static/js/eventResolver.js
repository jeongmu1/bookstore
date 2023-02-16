function selectAll(selectAll) {
    const checkboxes = document.getElementsByName('ids');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked;
    })
}
