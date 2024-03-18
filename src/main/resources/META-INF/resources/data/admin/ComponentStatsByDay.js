import Component from "./Component.js";
import DomBuilder from "./DomBuilder.js";
import { handle } from "./Utils.js";

/**
 * @version 2024-03-18
 * @author Patrik Harag
 */
export default class ComponentStatsByDay extends Component {

    #node = DomBuilder.div();

    refresh() {
        fetch('/api/admin/stats/by-day', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => response.json()).then(statsList => {
            const table = DomBuilder.bootstrapTableBuilder();

            function asDate(epochDay) {
                return new Date(epochDay * 86_400_000 + 10_000_000).toUTCString().substring(0, 17);
            }

            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('th', null, 'Id'),
                DomBuilder.element('th', null, 'Day'),
                DomBuilder.element('th', null, 'Updates'),
                DomBuilder.element('th', null, 'Completed'),
            ]));

            for (const stats of statsList) {
                table.addRow(DomBuilder.element('tr', null, [
                    DomBuilder.element('td', null, '' + stats.id),
                    DomBuilder.element('td', null, asDate(stats.id)),
                    DomBuilder.element('td', null, '' + stats.updates),
                    DomBuilder.element('td', null, '' + stats.completed),
                ]));
            }

            const total = DomBuilder.par();

            fetch('/api/admin/stats/by-day/sum', {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(response => response.json()).then(stats => {
                total.append(DomBuilder.span(`Total updates: ${stats.updates}`));
                total.append(DomBuilder.element('br'));
                total.append(DomBuilder.span(`Total completed: ${stats.completed}`));
            });

            this.#node.innerHTML = '';
            this.#node.append(table.createNode());
            this.#node.append(total);
        });
    }

    createNode() {
        return this.#node;
    }
}