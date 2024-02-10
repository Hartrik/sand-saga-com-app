import { DomBuilder } from "/data/admin/DomBuilder.js";
import { formatDate } from "/data/admin/Utils.js";

const root = document.getElementById('administration-root');

function handle(alertOnSuccess, alertOnFailure, fetchPromise, onSuccess = undefined) {
    fetchPromise.then(r => {
        if (r.ok) {
            return r.text();
        } else {
            throw r.status
        }
    }).then(r => {
        if (alertOnSuccess) {
            alert('Success');
        }
        if (onSuccess !== undefined) {
            onSuccess(r);
        }
    }).catch(e => {
        if (alertOnFailure) {
            alert('Error: ' + e);
        }
    });
}

// server status

const statusPar = DomBuilder.par({style: 'font-family: Consolas, monospace;'});
root.append(statusPar);
handle(false, false, fetch('/api/admin/server-status', {
    method: 'GET',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
}), result => {
    statusPar.innerText = Object.entries(JSON.parse(result)).map(([k, v]) => k + ': ' + v).join(',');
});

// buttons

root.append(DomBuilder.par(null, [
    DomBuilder.button('Reload Config', {class: 'btn btn-primary'}, () => {
        handle(true, true, fetch('/api/admin/reload-config', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: ''
        }));
    })
]));

// reports

const reportDiv = DomBuilder.div();
root.append(DomBuilder.element('h3', null, 'Reports'));
root.append(reportDiv);

function refreshReports() {
    fetch('/api/admin/reports', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    }).then(response => response.json()).then(reports => {
        const table = DomBuilder.bootstrapTableBuilder();

        function snapshotColumn(report) {
            if (report.snapshotSize === 0) {
                return 'n/a';
            }

            return [
                DomBuilder.span('' + report.snapshotSize + ' bytes ', {
                    style: 'white-space: nowrap;'
                }),
                DomBuilder.element('br'),
                DomBuilder.element('a', {
                    href: `/api/admin/reports/${report.id}/snapshot.sgjs`,
                    target: '_blank'
                }, 'Download')
            ];
        }

        function metadataColumn(report) {
            if (report.metadata === null || report.metadata === undefined || report.metadata.length === 0) {
                return 'n/a';
            }

            return [
                DomBuilder.link('Show', null, () => {
                    let dialog = DomBuilder.bootstrapDialogBuilder();
                    dialog.setHeaderContent('Metadata');
                    dialog.setBodyContent([
                        DomBuilder.par(null, report.metadata)
                    ]);
                    dialog.addCloseButton('Close');
                    dialog.show(root);
                })
            ];
        }

        function actionsColumn(report) {
            return DomBuilder.link('Delete', null, () => {
                handle(false, true, fetch('/api/admin/reports/' + report.id, {
                    method: 'DELETE',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }
                }), result => {
                    refreshReports();
                });
            })
        }

        table.addRow(DomBuilder.element('tr', null, [
            DomBuilder.element('th', null, 'ID'),
            DomBuilder.element('th', null, 'Date'),
            DomBuilder.element('th', null, 'Scenario'),
            DomBuilder.element('th', null, 'Type'),
            DomBuilder.element('th', null, 'Details'),
            DomBuilder.element('th', null, 'Metadata'),
            DomBuilder.element('th', null, 'Snapshot'),
            DomBuilder.element('th', null, 'IP'),
            DomBuilder.element('th', null, '_'),
        ]));

        for (const report of reports) {
            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('td', null, '' + report.id),
                DomBuilder.element('td', null, DomBuilder.span(formatDate(new Date(report.time)), {
                    style: 'white-space: nowrap;'
                })),
                DomBuilder.element('td', null, DomBuilder.span(report.scenario, {
                    style: 'white-space: nowrap;'
                })),
                DomBuilder.element('td', null, report.location),
                DomBuilder.element('td', null, [
                    DomBuilder.par(null, report.message)
                ]),
                DomBuilder.element('td', null, metadataColumn(report)),
                DomBuilder.element('td', null, snapshotColumn(report)),
                DomBuilder.element('td', null, report.ip),
                DomBuilder.element('td', null, actionsColumn(report)),
            ]));
        }

        reportDiv.innerHTML = '';
        reportDiv.append(table.createNode());
    });
}

refreshReports();
