"""Graph the results of Prioritetsko's testing program.

This script expects the testing results to be piped in through stdin.

Written by Tiger Sachse.
"""
import re
import sys
import matplotlib.pyplot as pyplot
from matplotlib.ticker import MaxNLocator


# A collection of constants that format the graph.
X_LABEL = "Threads"
Y_LABEL = "Milliseconds"
OUTPUT_FILE_NAME = "results.png"
GRAPH_TITLE = "Execution Time Per Thread"
HEADER_REGEX = r"^Execution time per thread for the (?P<queue_name>\w+):\n$"
DATA_LINE_REGEX = (
    r"^Threads:\s+(?P<thread_count>\d+)"
    r" \| "
    r"Milliseconds:\s+(?P<execution_time>[0-9.]+)\n$"
)


def get_graphs_from_stdin(header_regex, data_line_regex):
    """Get all graphs from stdin."""
    graphs = []
    for line in sys.stdin:

        # If the line is a header: create a new graph dictionary in the graphs
        # list and initialize it.
        header_match = re.match(header_regex, line)
        if header_match:
            graphs.append({})
            graphs[-1]["name"] = header_match.group("queue_name")
            graphs[-1]["execution_times"] = []

        # Otherwise if the line is a data line and the graphs list has at least
        # one graph in it: parse the data line's data and place it into the
        # latest graph.
        elif len(graphs) > 0:
            data_line_match = re.match(data_line_regex, line)
            if data_line_match:
                graphs[-1]["execution_times"].append(
                    float(data_line_match.group("execution_time")),
                )
            else:
                print("The data file's syntax is incorrect.")
                exit(1)
        else:
            print("The data file's syntax is incorrect.")
            exit(1)

    return graphs


def plot_graphs(graphs, x_label, y_label, graph_title, output_file_name):
    """Plot all graph data using Matplotlib."""

    # Set the x axis to always be whole numbers.
    axis = pyplot.figure().gca()
    axis.xaxis.set_major_locator(MaxNLocator(integer=True))

    for graph in graphs:
        pyplot.plot(
            [num for num in range(1, len(graph["execution_times"]) + 1)],
            graph["execution_times"],
            label=graph["name"],
        )

    pyplot.title(graph_title)
    pyplot.xlabel(x_label)
    pyplot.ylabel(y_label)
    pyplot.legend(loc="lower right")

    pyplot.savefig(output_file_name)


# Main entry point to this script.
graphs = get_graphs_from_stdin(HEADER_REGEX, DATA_LINE_REGEX)
plot_graphs(graphs, X_LABEL, Y_LABEL, GRAPH_TITLE, OUTPUT_FILE_NAME)
