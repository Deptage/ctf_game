import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import '../styles/ToDoList.css'

const flagTasks = [
  {
    id: 1,
    text: "Company flag 1",
    solved: false,
    flag_inputted: "",
  },
  {
    id: 5,
    text: "Forum flag 1",
    solved: false,
    flag_inputted: "",
  },
  {
    id: 6,
    text: "Forum flag 2",
    solved: false,
    flag_inputted: "",
  },
  {
    id: 7,
    text: "Messenger flag 1",
    solved: false,
    flag_inputted: "",
  },
  {
    id: 8,
    text: "Messenger flag 2",
    solved: false,
    flag_inputted: "",
  },
  {
    id: 2,
    text: "Bank flag 1",
    solved: false,
    flag_inputted: "",
  },
  {
    id: 3,
    text: "Bank flag 2",
    solved: false,
    flag_inputted: "",
  },
  {
    id: 4,
    text: "Bank flag 3",
    solved: false,
    flag_inputted: "",
  },
  
];

const TodoList = ({onFlagCorrect}) => {
  const [tasks, setTasks] = useState([]);
  const [editingTaskId, setEditingTaskId] = useState(null);
  const [flags, setFlags] = useState([]);
  const [incorrectFlag, setIncorrectFlag] = useState(false);
  const navigate = useNavigate();
  useEffect(() => {
    setTasks(flagTasks);
    flagSolvedInfoRequest();
  }, []);

  const updateScore = async () => {
    fetch("https://put-ctf-competition.put.poznan.pl/?rest_route=/score/v1/data", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
          username: localStorage.getItem("username"),
      }),
     })
      .then((response) => response.json())
      .then((data) => {
          
         localStorage.setItem("points", data.score);
         const storageEvent = new Event('storage');
         storageEvent.key = 'points';
         storageEvent.newValue = data.score;
         window.dispatchEvent(storageEvent);
      })
      .catch((error) => {
          console.error("Network or server error:", error);
      });
  };
  const showDelayedMessage = () => {
    setIncorrectFlag(true);
    setTimeout(() => {
      setIncorrectFlag(false);
    }, 2500);
  }
  const scoreRequest = async (flagId, flag) => {
    flag = flag.trim();
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.error("No JWT token found in localStorage.");
        return false;
      }
      const response = await fetch(`http://localhost:8080/flag/submit/${flagId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({
          "flag": `${flag}`,
        }),
      });

      if (response.ok) {
        const result = await response.json();
        if (result.correct) {
          console.log(result.message);
          const updatedTasks = tasks.map((task) =>
            task.id === flagId ? { ...task, solved: true } : task
          );
          setTasks(updatedTasks);
          await updateScore();
          return true;
        } else {
          console.log(result.message);
          if(result.message==="You have already completed this level!")
          {
            return "already completed";
          }
          return false;
        }
      } else {
        console.error("Invalid level id.");
      }
    } catch (error) {
      console.error("An error occurred:", error);
    }
  };

  const flagSolvedInfoRequest = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.error("No JWT token found in localStorage.");
        return false;
      }
      const response = await fetch("http://localhost:8080/flag/solvedInfo", {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
        }
      });
  
      if (response.ok) {
        const result = await response.json();
        setFlags(result);

        const updatedTasks = flagTasks.map((task) => {
          const solvedInfo = result.find((flag) => flag.id === task.id);
          return solvedInfo ? { ...task, solved: solvedInfo.solved } : task;
        });
        setTasks(updatedTasks);
      
      } else {
        console.error(`Error: ${response.status} - ${response.statusText}`);
      }
    } catch (error) {
      console.error("An error occurred:", error);
    }
  };

  const handleInputSubmit = async (taskId) => {
    const task = tasks.find((task) => task.id === taskId);
  
    if (task) {
      const result = await scoreRequest(task.id, task.flag_inputted);
      if (result === true) {
        taskCompleted(task.id);
        setIncorrectFlag(false);
        onFlagCorrect();
      } else {
        showDelayedMessage();
        setTasks((prevTasks) =>
          prevTasks.map((task) =>
            task.id === taskId ? { ...task, flag_inputted: "" } : task
          )
        );
      }
    }
  };

  const taskCompleted = (taskId) => {
    setTasks((prevTasks) =>
      prevTasks.map((task) =>
        task.id === taskId ? { ...task, DONE: true, flag_inputted: "" } : task
      )
    );
    setEditingTaskId(null);
  };

  const handleTaskClick = (task) => {
    setIncorrectFlag(false);
    setEditingTaskId(task.id);
  };

  const handleInputChange = (taskId, value) => {
    setTasks((prevTasks) =>
      prevTasks.map((task) => {
        if (task.id === taskId) {
          return { ...task, flag_inputted: value };
        }
        return task;
      })
    );
  };

  return (
    <div className="todo-list">
      <h1>Flags</h1>
      {tasks.map((task) => (
        <p key={task.id} className={task.solved ? "completed" : ""} onClick={() => handleTaskClick(task)}>
          {editingTaskId === task.id && !task.solved ? (
            <div className="inside-flag">
              {incorrectFlag && <div className="flag-warning">
                Incorrect flag!
              </div>}
              <div>
                <input
                  style={{
                    padding: '5px',
                    backgroundColor: 'black',
                    color: 'rgb(191, 250, 115)',
                    borderColor: 'rgb(142, 207, 98)'
                  }}
                  type="text"
                  value={task.flag_inputted}
                  onChange={(e) => handleInputChange(task.id, e.target.value)}
                  placeholder="Enter the FLAG"
                  disabled={task.solved}
                />
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    handleInputSubmit(task.id);
                  }}
                  style={{
                    padding: '5px',
                    backgroundColor: 'black',
                    color: 'rgb(191, 250, 115)',
                    borderColor: 'rgb(142, 207, 98)'
                  }}
                  disabled={task.solved}
                >
                  Submit
                </button>
              </div>
            </div>
          ) : (
            <span
              onClick={() => handleTaskClick(task)}
              style={{
                textDecoration: task.solved ? "line-through" : "none",
                cursor: task.solved ? "default" : "pointer",
              }}
            >
              {task.text}
            </span>
          )}
        </p>
      ))}
      <div className="visit-wiki" onClick={()=>navigate('/wiki')}>Need a hint? Visit the wiki</div>
    </div>
  );
};

export default TodoList;

