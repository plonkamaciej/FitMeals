import React, { useState } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from './ui/card';
import { Button } from './ui/button';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { Calendar } from './ui/calendar';
import { Link } from 'react-router-dom';

interface User {
  id: number;
  username: string;
  email: string;
  dailyCalorieRequirement: number;
  proteinGoal: number;
  carbsGoal: number;
  fatGoal: number;
}

interface Report {
  id: number;
  user: User;
  reportType: 'DAILY' | 'WEEKLY' | 'MONTHLY';
  startDate: string;
  endDate: string;
  totalCalories: number;
  totalProtein: number;
  totalCarbs: number;
  totalFat: number;
  createdAt: string;
}

function ProgressBar({ current, goal, label, color = "bg-blue-500", unit = "g" }: { 
  current: number; 
  goal: number; 
  label: string;
  color?: string;
  unit?: string;
}) {
  const percentage = Math.min((current / goal) * 100, 100);

  return (
    <div className="space-y-2">
      <div className="flex justify-between text-sm">
        <span>{label}</span>
        <span>{Math.round(current * 10) / 10} {unit} / {goal} {unit}</span>
      </div>
      <div className="h-2 w-full bg-gray-200 rounded-full overflow-hidden">
        <div 
          className={`h-full ${color} transition-all duration-300`} 
          style={{ width: `${percentage}%` }}
        />
      </div>
      <div className="text-right text-sm text-gray-500">
        {Math.round(percentage)}%
      </div>
    </div>
  );
}

function ReportsView() {
  const [startDate, setStartDate] = useState<Date>(new Date());
  const [endDate, setEndDate] = useState<Date>(new Date());
  const [report, setReport] = useState<Report | null>(null);

  const generateReport = async () => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');

    if (!userId || !token) {
      console.error('Brak userId lub tokenu');
      return;
    }

    // Ustawienie godzin dla dat z uwzględnieniem strefy czasowej
    const startDateTime = new Date(startDate);
    startDateTime.setHours(1, 0, 0, 0); // Ustawiamy na 1:00 żeby uniknąć problemów ze strefą czasową
    
    const endDateTime = new Date(endDate);
    endDateTime.setHours(23, 59, 0, 0);

    const requestData = {
      userId: Number(userId),
      reportType: "WEEKLY",
      startDate: startDateTime.toISOString().slice(0, 19), // Format: YYYY-MM-DDTHH:mm:ss
      endDate: endDateTime.toISOString().slice(0, 19)
    };

    console.log('Wysyłane dane:', requestData); // Dla debugowania

    try {
      const response = await fetch('http://localhost:8081/api/reports/generate', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error('Odpowiedź serwera:', errorText);
        throw new Error(`Błąd podczas generowania raportu: ${response.status}`);
      }

      const data = await response.json();
      setReport(data);
    } catch (error) {
      console.error('Błąd:', error);
    }
  };

  const getDaysDifference = (startDate: string, endDate: string) => {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end.getTime() - start.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays || 1; // Zwracamy minimum 1 dzień
  };

  return (
    <div className="container mx-auto p-4">
      <div className="flex  justify-between">       
        <h1 className="text-3xl font-bold mb-6">Raporty</h1>
        <Link className="text-xl font-bold mb-6" to="/diary">Powrót do dziennika</Link> 
      </div>

      <Card className="mb-6">
        <CardHeader>
          <CardTitle>Generuj raport</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 justify-items-center">
            <div>
              <label className="block text-sm font-medium mb-2">Data początkowa</label>
              <Calendar
                mode="single"
                selected={startDate}
                onSelect={(date) => date && setStartDate(date)}
                className="border rounded-md p-4"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Data końcowa</label>
              <Calendar
                mode="single"
                selected={endDate}
                onSelect={(date) => date && setEndDate(date)}
                className="border rounded-md p-4"
              />
            </div>
          </div>

          <Button onClick={generateReport} className="w-full">
            Generuj raport
          </Button>
        </CardContent>
      </Card>

      {report && (
        <Card>
          <CardHeader>
            <CardTitle>Raport {report.reportType.toLowerCase()}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <p className="text-sm text-gray-600">
                Okres: {new Date(report.startDate).toLocaleDateString('pl-PL')} - {new Date(report.endDate).toLocaleDateString('pl-PL')}
              </p>
              <p className="text-sm text-gray-600">
                Wygenerowano: {new Date(report.createdAt).toLocaleString('pl-PL')}
              </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Sekcja spożytych wartości całkowitych */}


              {/* Sekcja średnich dziennych wartości */}
              <div className="space-y-4">
                <h3 className="font-semibold text-lg">Średnie dzienne wartości</h3>
                <div className="grid grid-cols-2 gap-4">
                  <div className="p-4 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">Kalorie</p>
                    <p className="text-xl font-bold">
                      {Math.round(report.totalCalories / getDaysDifference(report.startDate, report.endDate))} kcal
                    </p>
                  </div>
                  <div className="p-4 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">Białko</p>
                    <p className="text-xl font-bold">
                      {Math.round(report.totalProtein / getDaysDifference(report.startDate, report.endDate))} g
                    </p>
                  </div>
                  <div className="p-4 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">Węglowodany</p>
                    <p className="text-xl font-bold">
                      {Math.round(report.totalCarbs / getDaysDifference(report.startDate, report.endDate))} g
                    </p>
                  </div>
                  <div className="p-4 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">Tłuszcze</p>
                    <p className="text-xl font-bold">
                      {Math.round(report.totalFat / getDaysDifference(report.startDate, report.endDate))} g
                    </p>
                  </div>
                </div>
              </div>

              {/* Sekcja celów dziennych */}
              <div className="space-y-4">
                <h3 className="font-semibold text-lg">Cele dzienne</h3>
                <div className="grid grid-cols-2 gap-4">
                  <div className="p-4 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">Cel kaloryczny</p>
                    <p className="text-xl font-bold">{report.user.dailyCalorieRequirement} kcal</p>
                  </div>
                  <div className="p-4 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">Cel białka</p>
                    <p className="text-xl font-bold">{report.user.proteinGoal} g</p>
                  </div>
                  <div className="p-4 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">Cel węglowodanów</p>
                    <p className="text-xl font-bold">{report.user.carbsGoal} g</p>
                  </div>
                  <div className="p-4 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">Cel tłuszczów</p>
                    <p className="text-xl font-bold">{report.user.fatGoal} g</p>
                  </div>
                </div>
              </div>

              {/* Sekcja realizacji celów (średnie dzienne wartości vs cele) */}
              <div className="md:col-span-2 space-y-4">
                <h3 className="font-semibold text-lg">Realizacja celów (średnie dzienne)</h3>
                <div className="space-y-4">
                  <ProgressBar
                    current={report.totalCalories / getDaysDifference(report.startDate, report.endDate)}
                    goal={report.user.dailyCalorieRequirement}
                    label="Kalorie"
                    color="bg-blue-500"
                    unit="kcal"
                  />
                  <ProgressBar
                    current={report.totalProtein / getDaysDifference(report.startDate, report.endDate)}
                    goal={report.user.proteinGoal}
                    label="Białko"
                    color="bg-red-500"
                    unit="g"
                  />
                  <ProgressBar
                    current={report.totalCarbs / getDaysDifference(report.startDate, report.endDate)}
                    goal={report.user.carbsGoal}
                    label="Węglowodany"
                    color="bg-green-500"
                    unit="g"
                  />
                  <ProgressBar
                    current={report.totalFat / getDaysDifference(report.startDate, report.endDate)}
                    goal={report.user.fatGoal}
                    label="Tłuszcze"
                    color="bg-yellow-500"
                    unit="g"
                  />
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
}

export default ReportsView;
